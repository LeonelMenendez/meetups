package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.request.MeetupCreationDto;
import io.github.lzmz.meetups.dto.response.MeetupAdminDto;
import io.github.lzmz.meetups.dto.response.MeetupUserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.exceptions.EntityNotFoundException;
import io.github.lzmz.meetups.model.MeetupModel;
import io.github.lzmz.meetups.model.UserModel;
import io.github.lzmz.meetups.repository.MeetupRepository;
import io.github.lzmz.meetups.repository.UserRepository;
import io.github.lzmz.meetups.service.MeetupService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MeetupServiceImpl implements MeetupService {

    private final MeetupRepository meetupRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public MeetupServiceImpl(MeetupRepository meetupRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.meetupRepository = meetupRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MeetupAdminDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        MeetupModel meetup = modelMapper.map(meetupCreationDto, MeetupModel.class);
        long ownerId = meetupCreationDto.getOwnerId();
        LocalDate day = meetup.getDay();

        if (meetupRepository.existsByOwnerIdAndDay(ownerId, day)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(ownerId, day), Arrays.asList("owner", "day"));
        }

        meetup.setOwner(userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(UserModel.class, ownerId)));
        meetupRepository.save(meetup);
        return toAdminDto(meetup);
    }

    @Override
    public int calculateNeededBeerCases(long meetupId) throws EntityNotFoundException {
        MeetupModel meetup = meetupRepository.findWithEnrolledUsersById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId));
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
        MeetupModel meetup = meetupRepository.findWithEnrolledUsersById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId));
        return meetup.getTemperature();
    }

    @Override
    public List<MeetupAdminDto> getCreatedMeetups(long ownerId) {
        List<MeetupModel> meetups = meetupRepository.findAllWithEnrolledUsersByOwnerId(ownerId);
        List<MeetupAdminDto> meetupAdminDtos = new ArrayList<>();
        meetups.forEach(meetup -> meetupAdminDtos.add(toAdminDto(meetup)));
        return meetupAdminDtos;
    }

    @Override
    public List<MeetupUserDto> getEnrolledMeetups(long userId) {
        List<MeetupModel> meetups = meetupRepository.findAllByEnrolledUsersUserId(userId);
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
