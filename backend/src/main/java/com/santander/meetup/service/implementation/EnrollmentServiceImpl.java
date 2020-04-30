package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.response.EnrollmentDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.EnrollmentModel;
import com.santander.meetup.repository.EnrollmentRepository;
import com.santander.meetup.service.EnrollmentService;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import jdk.vm.ci.meta.Local;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private MeetupService meetupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EnrollmentModel findById(Long id) throws EntityNotFoundException {
        return enrollmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EnrollmentModel.class, id));
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

        enrollment.setMeetup(meetupService.findById(enrollmentCreationDto.getMeetupId()));
        enrollment.setUser(userService.findById(enrollmentCreationDto.getUserId()));
        enrollmentRepository.save(enrollment);
        return toDto(enrollment);
    }

    @Override
    public void checkIn(long enrollmentId) throws EntityNotFoundException, ValueNotAllowedException {
        EnrollmentModel enrollment = findById(enrollmentId);

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
