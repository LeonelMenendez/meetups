package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.request.InvitationCreationDto;
import com.santander.meetup.dto.request.InvitationPatchDto;
import com.santander.meetup.dto.response.InvitationDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.InvitationModel;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.model.UserModel;
import com.santander.meetup.repository.InvitationRepository;
import com.santander.meetup.service.EnrollmentService;
import com.santander.meetup.service.InvitationService;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private MeetupService meetupService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public InvitationModel findById(Long id) throws EntityNotFoundException {
        return invitationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(InvitationModel.class, id));
    }

    @Override
    public List<InvitationDto> findAll(Long meetupId, Long userId, InvitationModel.Status status) {
        MeetupModel meetup = new MeetupModel();
        UserModel user = new UserModel();

        meetup.setId(meetupId);
        user.setId(userId);

        InvitationModel invitationExample = new InvitationModel();
        invitationExample.setMeetup(meetup);
        invitationExample.setUser(user);
        invitationExample.setStatus(status);
        List<InvitationModel> invitations = invitationRepository.findAll(Example.of(invitationExample));

        List<InvitationDto> invitationDtos = new ArrayList<>();
        invitations.forEach(invitation -> invitationDtos.add(toDto(invitation)));
        return invitationDtos;
    }

    @Override
    public boolean existsByMeetupIdAndUserId(Long meetupId, Long userId) {
        return invitationRepository.existsByMeetupIdAndUserIdAndStatusNot(meetupId, userId, InvitationModel.Status.DECLINED);
    }

    @Override
    public InvitationDto create(InvitationCreationDto invitationCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        InvitationModel invitation = modelMapper.map(invitationCreationDto, InvitationModel.class);
        long meetupId = invitationCreationDto.getMeetupId();
        long userId = invitationCreationDto.getUserId();

        if (existsByMeetupIdAndUserId(meetupId, userId)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(meetupId, userId), Arrays.asList("meetup", "user"));
        }

        invitation.setMeetup(meetupService.findById(invitationCreationDto.getMeetupId()));
        invitation.setUser(userService.findById(invitationCreationDto.getUserId()));
        invitation.setStatus(InvitationModel.Status.PENDING);
        invitationRepository.save(invitation);
        return toDto(invitation);
    }

    @Override
    public void patch(long invitationId, InvitationPatchDto invitationPatchDto) throws EntityNotFoundException, DuplicateEntityException, ValueNotAllowedException {
        if (invitationPatchDto.getStatus() == null) {
            return;
        }

        InvitationModel invitation = findById(invitationId);
        if (invitation.getStatus() == InvitationModel.Status.ACCEPTED) {
            throw new ValueNotAllowedException("status", invitationPatchDto.getStatus(), "the invitation was already accepted");
        }

        invitation.setStatus(invitationPatchDto.getStatus());
        if (invitation.getStatus() == InvitationModel.Status.ACCEPTED) {
            EnrollmentCreationDto enrollmentCreationDto = new EnrollmentCreationDto();
            enrollmentCreationDto.setMeetupId(invitation.getMeetup().getId());
            enrollmentCreationDto.setUserId(invitation.getUser().getId());
            enrollmentService.create(enrollmentCreationDto);
        }

        invitationRepository.save(invitation);
    }

    private InvitationDto toDto(InvitationModel invitation) {
        InvitationDto invitationDto = modelMapper.map(invitation, InvitationDto.class);
        invitationDto.setMeetupId(invitation.getMeetup().getId());
        invitationDto.setUserId(invitation.getUser().getId());
        return invitationDto;
    }
}
