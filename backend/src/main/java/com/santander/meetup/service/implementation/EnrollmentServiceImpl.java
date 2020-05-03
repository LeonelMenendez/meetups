package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.response.EnrollmentDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.EnrollmentModel;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.model.UserModel;
import com.santander.meetup.repository.EnrollmentRepository;
import com.santander.meetup.repository.MeetupRepository;
import com.santander.meetup.repository.UserRepository;
import com.santander.meetup.service.EnrollmentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final MeetupRepository meetupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, MeetupRepository meetupRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.meetupRepository = meetupRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EnrollmentDto> findAll(Long userId) {
        List<EnrollmentModel> enrollments = enrollmentRepository.findAllWithMeetupAndUserByUserId(userId);
        List<EnrollmentDto> enrollmentDtos = new ArrayList<>();
        enrollments.forEach(enrollment -> enrollmentDtos.add(toDto(enrollment)));
        return enrollmentDtos;
    }

    @Override
    public boolean existsByMeetupAndUser(Long meetupId, Long userId) {
        return enrollmentRepository.existsByMeetupIdAndUserId(meetupId, userId);
    }

    @Override
    public long countUsersEnrolled(long meetupId) {
        return enrollmentRepository.countByMeetupId(meetupId);
    }

    @Override
    public EnrollmentDto create(EnrollmentCreationDto enrollmentCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        EnrollmentModel enrollment = modelMapper.map(enrollmentCreationDto, EnrollmentModel.class);
        long meetupId = enrollmentCreationDto.getMeetupId();
        long userId = enrollmentCreationDto.getUserId();

        if (existsByMeetupAndUser(meetupId, userId)) {
            throw new DuplicateEntityException(EnrollmentModel.class, Arrays.asList(meetupId, userId), Arrays.asList("meetup", "user"));
        }

        enrollment.setMeetup(meetupRepository.findById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId)));
        enrollment.setUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(UserModel.class, userId)));
        enrollmentRepository.save(enrollment);
        return toDto(enrollment);
    }

    @Override
    public void checkIn(long enrollmentId) throws ValueNotAllowedException, EntityNotFoundException {
        EnrollmentModel enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new EntityNotFoundException(EnrollmentModel.class, enrollmentId));

        if (enrollment.getMeetup().getDay().isAfter(LocalDate.now())) {
            throw new ValueNotAllowedException("checked in", true, "the check-in can't be made before the meetup");
        }

        enrollment.setCheckedIn(true);
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentDto toDto(EnrollmentModel enrollment) {
        EnrollmentDto enrollmentDto = modelMapper.map(enrollment, EnrollmentDto.class);
        enrollmentDto.setMeetupId(enrollment.getMeetup().getId());
        enrollmentDto.setUserId(enrollment.getUser().getId());
        enrollmentDto.setMeetupOwnerName(enrollment.getMeetup().getOwner().getName());
        enrollmentDto.setMeetupOwnerEmail(enrollment.getMeetup().getOwner().getEmail());
        enrollmentDto.setMeetupDay(enrollment.getMeetup().getDay());
        enrollmentDto.setMeetupTemperature(enrollment.getMeetup().getTemperature());
        enrollmentDto.setCheckedIn(enrollment.isCheckedIn());
        return enrollmentDto;
    }
}
