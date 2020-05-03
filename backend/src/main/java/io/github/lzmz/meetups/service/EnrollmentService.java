package io.github.lzmz.meetups.service;

import io.github.lzmz.meetups.dto.request.EnrollmentCreationDto;
import io.github.lzmz.meetups.dto.response.EnrollmentDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.exceptions.EntityNotFoundException;
import io.github.lzmz.meetups.exceptions.ValueNotAllowedException;

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
