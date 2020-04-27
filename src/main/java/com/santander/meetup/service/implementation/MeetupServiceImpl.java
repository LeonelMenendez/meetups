package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.MeetupAdminDto;
import com.santander.meetup.dto.response.MeetupUserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.repository.MeetupRepository;
import com.santander.meetup.service.MeetupService;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MeetupServiceImpl implements MeetupService {

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MeetupModel findById(Long id) throws EntityNotFoundException {
        return meetupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, id));
    }

    @Override
    public MeetupModel findWithInscribedUsersById(Long id) throws EntityNotFoundException {
        return meetupRepository.findWithInscribedUsersById(id).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, id));
    }

    @Override
    public Iterable<MeetupModel> findAllWithInscribedUsersByOwnerId(Long ownerId) {
        return meetupRepository.findAllWithInscribedUsersByOwnerId(ownerId);
    }

    @Override
    public Iterable<MeetupModel> findAllByEnrolledUsersUserId(Long userId) {
        return meetupRepository.findAllByEnrolledUsersUserId(userId);
    }

    @Override
    public boolean existsById(Long id) {
        return meetupRepository.existsById(id);
    }

    @Override
    public boolean existsByOwnerIdAndDay(Long ownerId, LocalDate day) {
        return meetupRepository.existsByOwnerIdAndDay(ownerId, day);
    }

    @Override
    public MeetupUserDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        MeetupModel meetup = modelMapper.map(meetupCreationDto, MeetupModel.class);
        long ownerId = meetupCreationDto.getOwnerId();
        LocalDate day = meetup.getDay();

        if (existsByOwnerIdAndDay(ownerId, day)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(ownerId, day), Arrays.asList("owner", "day"));
        }

        meetup.setOwner(userService.findById(ownerId));
        meetupRepository.save(meetup);
        return toUserDto(meetup);
    }

    @Override
    public int calculateNeededBeerCases(long meetupId) throws EntityNotFoundException {
        MeetupModel meetup = findWithInscribedUsersById(meetupId);
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
        Iterable<MeetupModel> meetups = findAllWithInscribedUsersByOwnerId(ownerId);
        List<MeetupAdminDto> meetupAdminDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupAdminDtos.add(toAdminDto(meetup)));
        return meetupAdminDtos;
    }

    @Override
    public List<MeetupUserDto> getEnrolledMeetups(long userId) {
        Iterable<MeetupModel> meetups = findAllByEnrolledUsersUserId(userId);
        List<MeetupUserDto> meetupUserDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupUserDtos.add(toUserDto(meetup)));
        return meetupUserDtos;
    }

    private MeetupUserDto toUserDto(MeetupModel meetup) {
        MeetupUserDto meetupUserDto = modelMapper.map(meetup, MeetupUserDto.class);
        meetupUserDto.setOwnerId(meetup.getOwner().getId());
        return meetupUserDto;
    }

    private MeetupAdminDto toAdminDto(MeetupModel meetup) {
        MeetupAdminDto meetupAdminDto = modelMapper.map(meetup, MeetupAdminDto.class);
        meetupAdminDto.setOwnerId(meetup.getOwner().getId());
        meetupAdminDto.setBeerCasesNeeded(calculateNeededBeerCases(meetup.getTemperature(), meetup.getEnrolledUsers().size()));
        return meetupAdminDto;
    }
}
