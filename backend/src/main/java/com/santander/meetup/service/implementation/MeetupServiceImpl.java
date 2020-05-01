package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.InvitationCreationDto;
import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.InvitationDto;
import com.santander.meetup.dto.response.MeetupAdminDto;
import com.santander.meetup.dto.response.MeetupUserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.repository.MeetupRepository;
import com.santander.meetup.service.InvitationService;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MeetupServiceImpl implements MeetupService {

    private final MeetupRepository meetupRepository;
    private final UserService userService;
    private final InvitationService invitationService;
    private final ModelMapper modelMapper;

    public MeetupServiceImpl(MeetupRepository meetupRepository, UserService userService, @Lazy InvitationService invitationService, ModelMapper modelMapper) {
        this.meetupRepository = meetupRepository;
        this.userService = userService;
        this.invitationService = invitationService;
        this.modelMapper = modelMapper;
    }

    @Override
    public MeetupModel findById(Long id) throws EntityNotFoundException {
        return meetupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, id));
    }

    @Override
    public MeetupModel findByIdWithEnrolledUsers(Long id) throws EntityNotFoundException {
        return meetupRepository.findWithInscribedUsersById(id).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, id));
    }

    @Override
    public List<MeetupModel> findAllByOwnerWithEnrolledUsers(Long ownerId) {
        return meetupRepository.findAllWithInscribedUsersByOwnerId(ownerId);
    }

    @Override
    public List<MeetupModel> findAllByUser(Long userId) {
        return meetupRepository.findAllByEnrolledUsersUserId(userId);
    }

    @Override
    public boolean existsById(Long id) {
        return meetupRepository.existsById(id);
    }

    @Override
    public boolean existsByOwnerAndDay(Long ownerId, LocalDate day) {
        return meetupRepository.existsByOwnerIdAndDay(ownerId, day);
    }

    @Override
    public MeetupAdminDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        MeetupModel meetup = modelMapper.map(meetupCreationDto, MeetupModel.class);
        long ownerId = meetupCreationDto.getOwnerId();
        LocalDate day = meetup.getDay();

        if (existsByOwnerAndDay(ownerId, day)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(ownerId, day), Arrays.asList("owner", "day"));
        }

        meetup.setOwner(userService.findById(ownerId));
        meetupRepository.save(meetup);
        return toAdminDto(meetup);
    }

    @Override
    public List<InvitationDto> create(Long meetupId, List<Long> userIds) throws DuplicateEntityException, EntityNotFoundException {
        List<InvitationDto> invitationDtos = new ArrayList<>();

        for (Long userId : userIds) {
            InvitationCreationDto invitationCreationDto = new InvitationCreationDto();
            invitationCreationDto.setMeetupId(meetupId);
            invitationCreationDto.setUserId(userId);
            invitationDtos.add(invitationService.create(invitationCreationDto));
        }

        return invitationDtos;
    }

    @Override
    public int calculateNeededBeerCases(long meetupId) throws EntityNotFoundException {
        MeetupModel meetup = findByIdWithEnrolledUsers(meetupId);
        return calculateNeededBeerCases(meetup.getTemperature(), meetup.getEnrolledUsers().size());
    }

    public int calculateNeededBeerCases(double temperature, int participants) {
        double beersNeeded;

        if (temperature < 20) {
            beersNeeded = 0.75 * participants;
        } else if (temperature >= 20 && temperature <= 24) {
            beersNeeded = participants;
        } else {
            beersNeeded = 2 * participants;
        }

        return (int) Math.ceil(beersNeeded / 6);
    }

    @Override
    public double getTemperature(long meetupId) throws EntityNotFoundException {
        MeetupModel meetup = findById(meetupId);
        return meetup.getTemperature();
    }

    @Override
    public List<MeetupAdminDto> getCreatedMeetups(long ownerId) {
        List<MeetupModel> meetups = findAllByOwnerWithEnrolledUsers(ownerId);
        List<MeetupAdminDto> meetupAdminDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupAdminDtos.add(toAdminDto(meetup)));
        return meetupAdminDtos;
    }

    @Override
    public List<MeetupUserDto> getEnrolledMeetups(long userId) {
        List<MeetupModel> meetups = findAllByUser(userId);
        List<MeetupUserDto> meetupUserDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupUserDtos.add(toUserDto(meetup)));
        return meetupUserDtos;
    }

    private MeetupUserDto toUserDto(MeetupModel meetup) {
        MeetupUserDto meetupUserDto = modelMapper.map(meetup, MeetupUserDto.class);
        meetupUserDto.setOwnerId(meetup.getOwner().getId());
        meetupUserDto.setOwnerName(meetup.getOwner().getName());
        meetupUserDto.setOwnerEmail(meetup.getOwner().getEmail());
        return meetupUserDto;
    }

    private MeetupAdminDto toAdminDto(MeetupModel meetup) {
        MeetupAdminDto meetupAdminDto = modelMapper.map(meetup, MeetupAdminDto.class);
        meetupAdminDto.setOwnerId(meetup.getOwner().getId());
        meetupAdminDto.setOwnerName(meetup.getOwner().getName());
        meetupAdminDto.setOwnerEmail(meetup.getOwner().getEmail());
        meetupAdminDto.setBeerCasesNeeded(calculateNeededBeerCases(meetup.getTemperature(), meetup.getEnrolledUsers().size()));
        return meetupAdminDto;
    }
}
