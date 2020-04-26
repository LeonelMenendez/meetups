package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.response.EnrollmentDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.EnrollmentModel;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.repository.EnrollmentRepository;
import com.santander.meetup.service.EnrollmentService;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
    public boolean existsByMeetupIdAndUserId(Long meetupId, Long userId) {
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

        if (enrollmentRepository.existsByMeetupIdAndUserId(meetupId, userId)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(meetupId, userId), Arrays.asList("meetup", "user"));
        }

        enrollment.setMeetup(meetupService.findById(enrollmentCreationDto.getMeetupId()));
        enrollment.setUser(userService.findById(enrollmentCreationDto.getUserId()));
        enrollmentRepository.save(enrollment);
        return toDto(enrollment);
    }

    @Override
    public EnrollmentDto checkIn(long enrollmentId) throws EntityNotFoundException {
        EnrollmentModel enrollment = findById(enrollmentId);
        enrollment.setCheckedIn(true);
        enrollmentRepository.save(enrollment);
        return toDto(enrollment);
    }

    private EnrollmentDto toDto(EnrollmentModel enrollment) {
        EnrollmentDto enrollmentDto = modelMapper.map(enrollment, EnrollmentDto.class);
        enrollmentDto.setMeetupId(enrollment.getMeetup().getId());
        enrollmentDto.setUserId(enrollment.getUser().getId());
        return enrollmentDto;
    }
}
