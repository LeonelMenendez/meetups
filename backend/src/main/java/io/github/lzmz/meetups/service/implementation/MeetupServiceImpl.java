package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.mapper.MeetupMapper;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class MeetupServiceImpl implements MeetupService {

    private final MeetupRepository meetupRepository;
    private final UserRepository userRepository;
    private final MeetupMapper meetupMapper;

    public MeetupServiceImpl(MeetupRepository meetupRepository, UserRepository userRepository, MeetupMapper meetupMapper) {
        this.meetupRepository = meetupRepository;
        this.userRepository = userRepository;
        this.meetupMapper = meetupMapper;
    }

    @Override
    public MeetupAdminDto create(MeetupCreationDto meetupCreationDto) throws DuplicateEntityException, EntityNotFoundException {
        MeetupModel meetup = meetupMapper.meetupCreationDtoToMeetup(meetupCreationDto);
        long ownerId = meetupCreationDto.getOwnerId();
        LocalDate day = meetup.getDay();

        if (meetupRepository.existsByOwnerIdAndDay(ownerId, day)) {
            throw new DuplicateEntityException(MeetupModel.class, Arrays.asList(ownerId, day), Arrays.asList("owner", "day"));
        }

        meetup.setOwner(userRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(UserModel.class, ownerId)));
        meetupRepository.save(meetup);
        return meetupMapper.meetupToMeetupAdminDto(meetup);
    }

    @Override
    public int calculateBeerCasesNeeded(long meetupId) throws EntityNotFoundException {
        MeetupModel meetup = meetupRepository.findWithEnrolledUsersById(meetupId).orElseThrow(() -> new EntityNotFoundException(MeetupModel.class, meetupId));
        return calculateBeerCasesNeeded(meetup.getTemperature(), meetup.getEnrolledUsers().size());
    }

    /**
     * Calculates the beer cases needed based on temperature and number of participants.
     *
     * @param temperature  the temperature that will be used to calculate the required beer cases.
     * @param participants the number of participants that will be used to calculate the required beer cases.
     * @return the beer cases needed.
     */
    public int calculateBeerCasesNeeded(double temperature, int participants) {
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
        meetups.forEach(meetup -> meetup.setBeerCasesNeeded(calculateBeerCasesNeeded(meetup.getTemperature(), meetup.getEnrolledUsers().size())));
        return meetupMapper.meetupsToMeetupAdminDtos(meetups);
    }

    @Override
    public List<MeetupUserDto> getEnrolledMeetups(long userId) {
        List<MeetupModel> meetups = meetupRepository.findAllByEnrolledUsersUserId(userId);
        return meetupMapper.meetupsToMeetupUserDtos(meetups);
    }
}
