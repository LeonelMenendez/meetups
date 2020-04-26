package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.MeetupDto;
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
    public MeetupDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        MeetupModel meetup = modelMapper.map(meetupCreationDto, MeetupModel.class);
        long ownerId = meetupCreationDto.getOwnerId();
        LocalDate day = meetup.getDay();

        if (existsByOwnerIdAndDay(ownerId, day)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(ownerId, day), Arrays.asList("owner", "day"));
        }

        meetup.setOwner(userService.findById(ownerId));
        meetupRepository.save(meetup);
        return toDto(meetup);
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
    public List<MeetupDto> getCreatedMeetups(long ownerId) {
        Iterable<MeetupModel> meetups = findAllWithInscribedUsersByOwnerId(ownerId);
        List<MeetupDto> meetupDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupDtos.add(toDtoWithBeerCasesNeeded(meetup)));
        return meetupDtos;
    }

    @Override
    public List<MeetupDto> getEnrolledMeetups(long userId) {
        Iterable<MeetupModel> meetups = findAllByEnrolledUsersUserId(userId);
        List<MeetupDto> meetupDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupDtos.add(toDto(meetup)));
        return meetupDtos;
    }

    private MeetupDto toDto(MeetupModel meetup) {
        MeetupDto meetupDto = modelMapper.map(meetup, MeetupDto.class);
        meetupDto.setOwnerId(meetup.getOwner().getId());
        return meetupDto;
    }

    private MeetupDto toDtoWithBeerCasesNeeded(MeetupModel meetup) {
        MeetupDto meetupDto = toDto(meetup);
        meetupDto.setBeerCasesNeeded(calculateNeededBeerCases(meetup.getTemperature(), meetup.getEnrolledUsers().size()));
        return meetupDto;
    }
}
