package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.MeetupCreationDto;
import com.santander.meetup.dto.response.MeetupDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.MeetupModel;
import com.santander.meetup.repository.MeetupRepository;
import com.santander.meetup.service.MeetupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class MeetupServiceImpl implements MeetupService {

    @Autowired
    private MeetupRepository meetupRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MeetupModel findById(Long id) throws EntityNotFoundException {
        return meetupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, id));
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
    public MeetupDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException {
        MeetupModel meetup = modelMapper.map(meetupCreationDto, MeetupModel.class);
        long ownerId = meetup.getOwner().getId();
        LocalDate day = meetup.getDay();

        if (existsByOwnerIdAndDay(ownerId, day)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(ownerId, day), Arrays.asList("owner", "day"));
        }

        meetupRepository.save(meetup);
        return toDto(meetup);
    }

    @Override
    public int calculateRequiredBeerCases(long meetupId) throws EntityNotFoundException {
        MeetupModel meetup = findById(meetupId);
        return calculateNeededBeerCases(meetup.getTemperature(), meetup.getInscribedUsers().size());
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

    private MeetupDto toDto(MeetupModel meetup) {
        MeetupDto meetupDto = modelMapper.map(meetup, MeetupDto.class);
        meetupDto.setOwnerId(meetup.getOwner().getId());
        meetupDto.setBeerCasesNeeded(calculateNeededBeerCases(meetup.getTemperature(), meetup.getInscribedUsers().size()));
        return meetupDto;
    }
}
