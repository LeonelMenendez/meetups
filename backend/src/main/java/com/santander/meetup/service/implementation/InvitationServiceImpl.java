package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.EnrollmentCreationDto;
import com.santander.meetup.dto.request.InvitationCreationDto;
import com.santander.meetup.dto.request.InvitationStatusDto;
import com.santander.meetup.dto.response.InvitationDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.exceptions.ValueNotAllowedException;
import com.santander.meetup.model.InvitationModel;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.model.UserModel;
import com.santander.meetup.repository.InvitationRepository;
import com.santander.meetup.repository.MeetupRepository;
import com.santander.meetup.repository.UserRepository;
import com.santander.meetup.service.EnrollmentService;
import com.santander.meetup.service.InvitationService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final MeetupRepository meetupRepository;
    private final UserRepository userRepository;
    private final EnrollmentService enrollmentService;
    private final ModelMapper modelMapper;

    public InvitationServiceImpl(InvitationRepository invitationRepository, MeetupRepository meetupRepository, UserRepository userRepository, EnrollmentService enrollmentService, ModelMapper modelMapper) {
        this.invitationRepository = invitationRepository;
        this.meetupRepository = meetupRepository;
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean existsByMeetupAndUser(Long meetupId, Long userId) {
        return invitationRepository.existsByMeetupIdAndUserId(meetupId, userId);
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
    public InvitationDto create(InvitationCreationDto invitationCreationDto) throws DuplicateEntityException, EntityNotFoundException, ValueNotAllowedException {
        InvitationModel invitation = modelMapper.map(invitationCreationDto, InvitationModel.class);
        long meetupId = invitationCreationDto.getMeetupId();
        long userId = invitationCreationDto.getUserId();

        if (existsByMeetupAndUser(meetupId, userId)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(meetupId, userId), Arrays.asList("meetup", "user"));
        }

        invitation.setMeetup(meetupRepository.findById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId)));
        invitation.setUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(UserModel.class, userId)));
        invitation.setStatus(InvitationModel.Status.PENDING);
        invitationRepository.save(invitation);
        return toDto(invitation);
    }

    @Override
    public List<InvitationDto> create(Long meetupId, List<Long> userIds) throws DuplicateEntityException, EntityNotFoundException, ValueNotAllowedException {
        List<InvitationDto> invitationDtos = new ArrayList<>();

        for (Long userId : userIds) {
            InvitationCreationDto invitationCreationDto = new InvitationCreationDto();
            invitationCreationDto.setMeetupId(meetupId);
            invitationCreationDto.setUserId(userId);
            invitationDtos.add(create(invitationCreationDto));
        }

        return invitationDtos;
    }

    @Override
    public void changeStatus(long invitationId, InvitationStatusDto invitationStatusDto) throws EntityNotFoundException, DuplicateEntityException, ValueNotAllowedException {
        if (invitationStatusDto.getStatus() == null) {
            return;
        }

        InvitationModel invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new EntityNotFoundException(InvitationModel.class, invitationId));

        if (invitation.getStatus() == InvitationModel.Status.ACCEPTED) {
            throw new ValueNotAllowedException("status", invitationStatusDto.getStatus(), "the invitation was already accepted");
        }

        invitation.setStatus(invitationStatusDto.getStatus());
        if (invitation.getStatus() == InvitationModel.Status.ACCEPTED) {
            EnrollmentCreationDto enrollmentCreationDto = new EnrollmentCreationDto();
            enrollmentCreationDto.setMeetupId(invitation.getMeetup().getId());
            enrollmentCreationDto.setUserId(invitation.getUser().getId());
            this.enrollmentService.create(enrollmentCreationDto);
        }

        this.invitationRepository.save(invitation);
    }

    private InvitationDto toDto(InvitationModel invitation) {
        InvitationDto invitationDto = modelMapper.map(invitation, InvitationDto.class);
        invitationDto.setMeetupId(invitation.getMeetup().getId());
        invitationDto.setUserId(invitation.getUser().getId());
        invitationDto.setMeetupOwnerName(invitation.getMeetup().getOwner().getName());
        invitationDto.setMeetupOwnerEmail(invitation.getMeetup().getOwner().getEmail());
        invitationDto.setMeetupDay(invitation.getMeetup().getDay());
        invitationDto.setMeetupTemperature(invitation.getMeetup().getTemperature());
        return invitationDto;
    }
}
