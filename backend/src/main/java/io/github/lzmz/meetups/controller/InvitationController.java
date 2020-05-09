package io.github.lzmz.meetups.controller;

import io.github.lzmz.meetups.dto.request.InvitationCreationDto;
import io.github.lzmz.meetups.dto.request.InvitationStatusDto;
import io.github.lzmz.meetups.dto.response.InvitationDto;
import io.github.lzmz.meetups.endpoint.InvitationEndpoint;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.exceptions.EntityNotFoundException;
import io.github.lzmz.meetups.exceptions.ValueNotAllowedException;
import io.github.lzmz.meetups.model.InvitationModel;
import io.github.lzmz.meetups.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Invitations")
@RestController
@RequestMapping(value = InvitationEndpoint.BASE)
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    /**
     * Creates a new invitation.
     *
     * @param invitationCreationDto the invitation creation request body.
     * @return the created invitation.
     * @throws DuplicateEntityException if the invitation already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    @Operation(summary = "Creates a new invitation")
    @PostMapping()
    public ResponseEntity<InvitationDto> create(@Valid @RequestBody InvitationCreationDto invitationCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        return new ResponseEntity<>(invitationService.create(invitationCreationDto), HttpStatus.CREATED);
    }

    /**
     * Finds all the invitations for the given parameters.
     *
     * @param meetupId the meetup that will be filtered.
     * @param userId   the user that will be filtered.
     * @param status   the status that will be filtered.
     * @return a list of invitations filtered by the given parameters.
     */
    @Operation(summary = "Retrieves a list of invitations filtered by the given parameters")
    @GetMapping()
    public ResponseEntity<List<InvitationDto>> findAll(@Valid @RequestParam(required = false) Long meetupId,
                                                       @Valid @RequestParam(required = false) Long userId,
                                                       @Valid @RequestParam(required = false) InvitationModel.Status status) {
        return new ResponseEntity<>(invitationService.findAll(meetupId, userId, status), HttpStatus.OK);
    }

    /**
     * Changes an invitation status.
     *
     * @param invitationId        the id of the invitation that will be patched.
     * @param invitationStatusDto the invitation status request body.
     * @throws EntityNotFoundException  if the invitation wasn't found.
     * @throws DuplicateEntityException if the user is already enrolled in the meetup of the invitation.
     * @throws ValueNotAllowedException if the invitation was already accepted.
     */
    @Operation(summary = "Changes an invitation status", description = "If the invitation is accepted a new enrollment will be created")
    @PatchMapping(InvitationEndpoint.INVITATION_STATUS)
    public ResponseEntity<Void> patch(@Valid @PathVariable long invitationId, @Valid @RequestBody InvitationStatusDto invitationStatusDto) throws EntityNotFoundException, DuplicateEntityException, ValueNotAllowedException {
        invitationService.changeStatus(invitationId, invitationStatusDto);
        return ResponseEntity.noContent().build();
    }
}
