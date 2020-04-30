package com.santander.meetup.service;

import com.santander.meetup.dto.request.InvitationCreationDto;
import com.santander.meetup.dto.request.InvitationPatchDto;
import com.santander.meetup.dto.response.InvitationDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.InvitationModel;

import java.util.List;

public interface InvitationService {

    /**
     * Finds an invitation by id.
     *
     * @param id the id to be found.
     * @return an invitation with the given id.
     * @throws EntityNotFoundException if the invitation wasn't found.
     */
    InvitationModel findById(Long id) throws EntityNotFoundException;

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
     * Determines if a invitation already exists by the given user and meetup.
     * <p>An invitation can be created for a pair meetup-user if all the existing
     * invitations for that same pair don't have the status
     * {@link com.santander.meetup.model.InvitationModel.Status#DECLINED}.</p>
     *
     * @param meetupId the meetup id to be found.
     * @param userId   the user id to be found.
     * @return {@code true} if the invitation exists. {@code false} otherwise.
     */
    boolean existsByMeetupAndUser(Long meetupId, Long userId);

    /**
     * Creates a new invitation.
     *
     * @param invitationCreationDto the invitation creation data.
     * @return the created invitation.
     * @throws DuplicateEntityException if the invitation already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    InvitationDto create(InvitationCreationDto invitationCreationDto) throws DuplicateEntityException, EntityNotFoundException;

    /**
     * Patches an invitation.
     *
     * @param invitationId       the id of the invitation that will be patched.
     * @param invitationPatchDto the invitation patch data.
     * @throws EntityNotFoundException  if the invitation wasn't found.
     * @throws DuplicateEntityException if the user is already enrolled in the meetup of the invitation.
     * @throws ValueNotAllowedException if the invitation was already accepted.
     */
    void patch(long invitationId, InvitationPatchDto invitationPatchDto) throws EntityNotFoundException, DuplicateEntityException, ValueNotAllowedException;
}
