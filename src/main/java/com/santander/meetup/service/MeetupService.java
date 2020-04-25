package com.santander.meetup.service;

import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.MeetupDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.MeetupModel;

import java.time.LocalDate;

public interface MeetupService {

    /**
     * Finds a meetup by id.
     *
     * @param id the id to be found.
     * @return a meetup with the given id.
     * @throws EntityNotFoundException if the meetup wasn't found.
     */
    MeetupModel findById(Long id) throws EntityNotFoundException;

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
    boolean existsByOwnerIdAndDay(Long ownerId, LocalDate day);

    /**
     * Creates a new meetup.
     *
     * @param meetupCreationDto the meetup creation data.
     * @return the created meetup.
     * @throws DuplicateEntityException if the meetup already exists.
     */
    MeetupDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException;

    /**
     * Calculates the amount of beer cases needed for the given meetup.
     *
     * @param meetupId the id of the meetup from which the number of beer cases needed will be calculated.
     * @return The amount of beer cases needed.
     * @throws EntityNotFoundException if the meetup wasn't found.
     */
    int calculateRequiredBeerCases(long meetupId) throws EntityNotFoundException;

    /**
     * Calculates de beer cases needed based on temperature and number of participants.
     *
     * @param temperature  the temperature that will be used to calculate the required beer cases.
     * @param participants the number of participants that will be used to calculate the required beer cases.
     * @return the beer cases needed.
     */
    int calculateNeededBeerCases(double temperature, int participants);
}
