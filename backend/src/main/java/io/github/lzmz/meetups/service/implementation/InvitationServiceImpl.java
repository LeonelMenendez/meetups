package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.mapper.InvitationMapper;
import io.github.lzmz.meetups.dto.request.EnrollmentCreationDto;
import io.github.lzmz.meetups.dto.request.InvitationCreationDto;
import io.github.lzmz.meetups.dto.request.InvitationStatusDto;
import io.github.lzmz.meetups.dto.response.InvitationDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.exceptions.EntityNotFoundException;
import io.github.lzmz.meetups.exceptions.ValueNotAllowedException;
import io.github.lzmz.meetups.model.InvitationModel;
import io.github.lzmz.meetups.model.MeetupModel;
import io.github.lzmz.meetups.model.UserModel;
import io.github.lzmz.meetups.repository.InvitationRepository;
import io.github.lzmz.meetups.repository.MeetupRepository;
import io.github.lzmz.meetups.repository.UserRepository;
import io.github.lzmz.meetups.service.EnrollmentService;
import io.github.lzmz.meetups.service.InvitationService;
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
    private final InvitationMapper invitationMapper;

    public InvitationServiceImpl(InvitationRepository invitationRepository, MeetupRepository meetupRepository, UserRepository userRepository, EnrollmentService enrollmentService, InvitationMapper invitationMapper) {
        this.invitationRepository = invitationRepository;
        this.meetupRepository = meetupRepository;
        this.userRepository = userRepository;
        this.enrollmentService = enrollmentService;
        this.invitationMapper = invitationMapper;
    }

    @Override
    public List<InvitationDto> findAll(Long meetupId, Long userId, InvitationModel.Status status) {
        MeetupModel meetup = new MeetupModel();
        meetup.setId(meetupId);

        UserModel user = new UserModel();
        user.setId(userId);

        InvitationModel invitationExample = new InvitationModel();
        invitationExample.setMeetup(meetup);
        invitationExample.setUser(user);
        invitationExample.setStatus(status);

        List<InvitationModel> invitations = invitationRepository.findAll(Example.of(invitationExample));
        return invitationMapper.invitationsToInvitationDtos(invitations);
    }

    @Override
    public InvitationDto create(InvitationCreationDto invitationCreationDto) throws DuplicateEntityException, EntityNotFoundException, ValueNotAllowedException {
        InvitationModel invitation = invitationMapper.invitationCreationDtoToInvitation(invitationCreationDto);
        long meetupId = invitationCreationDto.getMeetupId();
        long userId = invitationCreationDto.getUserId();

        if (invitationRepository.existsByMeetupIdAndUserId(meetupId, userId)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(meetupId, userId), Arrays.asList("meetup", "user"));
        }

        invitation.setMeetup(meetupRepository.findById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId)));
        invitation.setUser(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(UserModel.class, userId)));
        invitation.setStatus(InvitationModel.Status.PENDING);
        invitationRepository.save(invitation);
        return invitationMapper.invitationToInvitationDto(invitation);
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
}
