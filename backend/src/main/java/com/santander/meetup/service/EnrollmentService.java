package com.santander.meetup.service;

import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.response.EnrollmentDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;

import java.util.List;

public interface EnrollmentService {

    /**
     * Finds all the enrollments of the given user.
     *
     * @param userId the user id to be found.
     * @return a list of enrollments of the given user.
     */
    List<EnrollmentDto> findAll(Long userId);

    /**
     * Determines if an enrollment already exists by the given meetup and user.
     *
     * @param meetupId the meetup id to be found
     * @param userId   the user id to be found
     * @return {@code true} if the enrollment exists. {@code false} otherwise.
     */
    boolean existsByMeetupAndUser(Long meetupId, Long userId);

    /**
     * Counts the number of users enrolled to the given meetup.
     *
     * @param meetupId the meetup id of which the enrolled users will be counted.
     * @return the number of users enrolled in the meetup.
     */
    long countUsersEnrolled(long meetupId);

    /**
     * Creates a new enrollment.
     *
     * @param enrollmentCreationDto the enrollment creation data.
     * @return the created enrollment.
     * @throws DuplicateEntityException if the enrollment already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    EnrollmentDto create(EnrollmentCreationDto enrollmentCreationDto) throws DuplicateEntityException, EntityNotFoundException;

    /**
     * Makes the check-in of the user associated to the given enrollment.
     *
     * @param enrollmentId the enrollment id for which will be made the check-in.
     * @throws EntityNotFoundException  if the enrollment wasn't found.
     * @throws ValueNotAllowedException if the check-in couldn't be made.
     */
    void checkIn(long enrollmentId) throws EntityNotFoundException, ValueNotAllowedException;
}
