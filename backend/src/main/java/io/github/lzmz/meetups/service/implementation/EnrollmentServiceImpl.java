package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.mapper.EnrollmentMapper;
import io.github.lzmz.meetups.dto.request.EnrollmentCreationDto;
import io.github.lzmz.meetups.dto.response.EnrollmentDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.exceptions.EntityNotFoundException;
import io.github.lzmz.meetups.exceptions.ValueNotAllowedException;
import io.github.lzmz.meetups.model.EnrollmentModel;
import io.github.lzmz.meetups.model.MeetupModel;
import io.github.lzmz.meetups.model.UserModel;
import io.github.lzmz.meetups.repository.EnrollmentRepository;
import io.github.lzmz.meetups.repository.MeetupRepository;
import io.github.lzmz.meetups.repository.UserRepository;
import io.github.lzmz.meetups.service.EnrollmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final MeetupRepository meetupRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, MeetupRepository meetupRepository, UserRepository userRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.meetupRepository = meetupRepository;
        this.userRepository = userRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    public List<EnrollmentDto> findAll(Long userId) {
        List<EnrollmentModel> enrollments = enrollmentRepository.findAllWithMeetupAndUserByUserId(userId);
        return enrollmentMapper.enrollmentsToEnrollmentDtos(enrollments);
    }

    @Override
    public EnrollmentDto create(EnrollmentCreationDto enrollmentCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        EnrollmentModel enrollment = enrollmentMapper.enrollmentCreationDtoToEnrollment(enrollmentCreationDto);
        long meetupId = enrollmentCreationDto.getMeetupId();
        long userId = enrollmentCreationDto.getUserId();

        if (enrollmentRepository.existsByMeetupIdAndUserId(meetupId, userId)) {
            throw new DuplicateEntityException(EnrollmentModel.class, Arrays.asList(meetupId, userId), Arrays.asList("meetup", "user"));
        }

        enrollment.setMeetup(meetupRepository.findById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId)));
        enrollment.setUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(UserModel.class, userId)));
        enrollmentRepository.save(enrollment);
        return enrollmentMapper.enrollmentToEnrollmentDto(enrollment);
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
}
