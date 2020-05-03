package com.santander.meetup.service;

import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.MeetupAdminDto;
import com.santander.meetup.dto.response.MeetupUserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MeetupService {

    /**
     * Determines if a meetup already exists by the given id.
     *
     * @param id the meetup id to be found.
     * @return {@code true} if the meetup exists. {@code false} otherwise.
     */
    boolean existsById(Long id);

    /**
     * Determines if a meetup already exists by the given email and day.
     *
     * @param ownerId the owner id to be found.
     * @param day     the day to be found.
     * @return {@code true} if the meetup exists. {@code false} otherwise.
     */
    boolean existsByOwnerAndDay(Long ownerId, LocalDate day);

    /**
     * Creates a new meetup.
     *
     * @param meetupCreationDto the meetup creation data.
     * @return the created meetup.
     * @throws DuplicateEntityException if the meetup already exists.
     * @throws EntityNotFoundException  if the given owner user wasn't found.
     */
    MeetupAdminDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException, EntityNotFoundException;

    /**
     * Calculates the amount of beer cases needed for the given meetup.
     *
     * @param meetupId the meetup id from which the number of beer cases needed will be calculated.
     * @return the amount of beer cases needed.
     * @throws EntityNotFoundException if the meetup wasn't found.
     */
    int calculateNeededBeerCases(long meetupId) throws EntityNotFoundException;

    /**
     * Calculates the beer cases needed based on temperature and number of participants.
     *
     * @param temperature  the temperature that will be used to calculate the required beer cases.
     * @param participants the number of participants that will be used to calculate the required beer cases.
     * @return the beer cases needed.
     */
    int calculateNeededBeerCases(double temperature, int participants);

    /**
     * Retrieves the day's temperature of the meeting.
     *
     * @param meetupId the meetup id from which the day's temperature will be retrieved.
     * @return the day's temperature of the meeting.
     * @throws EntityNotFoundException if the meetup wasn't found.
     */
    double getTemperature(long meetupId) throws EntityNotFoundException;

    /**
     * Retrieves the meetups created by the given user.
     *
     * @param ownerId the owner id of the meetups to retrieve.
     * @return a list of the meetups created by the given user.
     */
    List<MeetupAdminDto> getCreatedMeetups(long ownerId);

    /**
     * Retrieves the meetups in which the given user is enrolled.
     *
     * @param userId the user id of the meetups to retrieve.
     * @return a list of meetups in which the given user is enrolled.
     */
    List<MeetupUserDto> getEnrolledMeetups(long userId);
}
