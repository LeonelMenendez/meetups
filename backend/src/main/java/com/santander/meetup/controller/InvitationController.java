package com.santander.meetup.controller;

import com.santander.meetup.dto.request.InvitationCreationDto;
import com.santander.meetup.dto.request.InvitationPatchDto;
import com.santander.meetup.dto.response.InvitationDto;
import com.santander.meetup.endpoint.InvitationEndpoint;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.InvitationModel;
import com.santander.meetup.service.InvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(tags = "Invitations")
@RestController
@RequestMapping(value = InvitationEndpoint.BASE)
public class InvitationController {

    @Autowired
    InvitationService invitationService;

    /**
     * Creates a new invitation.
     *
     * @param invitationCreationDto the invitation creation request body.
     * @return the created invitation.
     * @throws DuplicateEntityException if the invitation already exists.
     * @throws EntityNotFoundException  if the given meetup or user wasn't found.
     */
    @ApiOperation(value = "Creates a new invitation")
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
    @ApiOperation(value = "Retrieves a list of invitations filtered by the given parameters")
    @GetMapping()
    public ResponseEntity<List<InvitationDto>> findAll(@Valid @RequestParam(required = false) Long meetupId,
                                                       @Valid @RequestParam(required = false) Long userId,
                                                       @Valid @RequestParam(required = false) InvitationModel.Status status) {
        return new ResponseEntity<>(invitationService.findAll(meetupId, userId, status), HttpStatus.OK);
    }

    /**
     * Patches an invitation.
     *
     * @param invitationId       the id of the invitation that will be patched.
     * @param invitationPatchDto the invitation patch request body.
     * @throws EntityNotFoundException  if the invitation wasn't found.
     * @throws DuplicateEntityException if the user is already enrolled in the meetup of the invitation.
     * @throws ValueNotAllowedException if the invitation was already accepted.
     */
    @ApiOperation(value = "Patches an invitation")
    @PatchMapping(InvitationEndpoint.INVITATION)
    public ResponseEntity<Void> patch(@Valid @PathVariable long invitationId, @Valid @RequestBody InvitationPatchDto invitationPatchDto) throws EntityNotFoundException, DuplicateEntityException, ValueNotAllowedException {
        invitationService.patch(invitationId, invitationPatchDto);
        return ResponseEntity.noContent().build();
    }
}
