package com.santander.meetup.service;

import com.santander.meetup.dto.request.InvitationCreationDto;
import com.santander.meetup.dto.request.InvitationStatusDto;
import com.santander.meetup.dto.response.InvitationDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.InvitationModel;

import java.util.List;

public interface InvitationService {

    /**
     * Determines if a invitation already exists by the given user and meetup.
     *
     * @param meetupId the meetup id to be found.
     * @param userId   the user id to be found.
     * @return {@code true} if the invitation exists. {@code false} otherwise.
     */
    boolean existsByMeetupAndUser(Long meetupId, Long userId);

    /**
     * Finds all the invitations for the given parameters.
     *
     * @param meetupId the meetup that will be filtered.
     * @param userId   the user that will be filtered.
     * @param status   the status that will be filtered.
     * @return a list of invitations filtered by the given parameters.
     */
    List<InvitationDto> findAll(Long meetupId, Long userId, InvitationModel.Status status);

    /**
     * Creates a new invitation.
     *
     * @param invitationCreationDto the invitation creation data.
     * @return the created invitation.
     * @throws DuplicateEntityException if the invitation already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    InvitationDto create(InvitationCreationDto invitationCreationDto) throws DuplicateEntityException, EntityNotFoundException, ValueNotAllowedException;

    /**
     * Creates a list of new invitations for the given meetup.
     *
     * @param meetupId the meetup id to which the invitations will be sent.
     * @param userIds  a list with the user's ids that will be invited.
     * @return the created invitations.
     * @throws DuplicateEntityException if one of the given invitations already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    List<InvitationDto> create(Long meetupId, List<Long> userIds) throws DuplicateEntityException, EntityNotFoundException, ValueNotAllowedException;

    /**
     * Changes an invitation status.
     * <p>If the invitation is accepted a new enrollment will be created.</p>
     *
     * @param invitationId        the id of the invitation that will be patched with the new status.
     * @param invitationStatusDto the invitation patch data.
     * @throws EntityNotFoundException  if the invitation wasn't found.
     * @throws DuplicateEntityException if the user is already enrolled in the meetup of the invitation.
     * @throws ValueNotAllowedException if the invitation was already accepted or it's still pending.
     */
    void changeStatus(long invitationId, InvitationStatusDto invitationStatusDto) throws EntityNotFoundException, DuplicateEntityException, ValueNotAllowedException;
}
