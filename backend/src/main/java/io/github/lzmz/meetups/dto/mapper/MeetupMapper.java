package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.MeetupCreationDto;
import io.github.lzmz.meetups.dto.response.MeetupAdminDto;
import io.github.lzmz.meetups.dto.response.MeetupUserDto;
import io.github.lzmz.meetups.model.MeetupModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MeetupMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    MeetupUserDto meetupToMeetupUserDto(MeetupModel meetup);

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "owner.email", target = "ownerEmail")
    MeetupAdminDto meetupToMeetupAdminDto(MeetupModel meetup);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "enrolledUsers", ignore = true)
    @Mapping(target = "beerCasesNeeded", ignore = true)
    MeetupModel meetupCreationDtoToMeetup(MeetupCreationDto meetupCreationDto);

    List<MeetupUserDto> meetupsToMeetupUserDtos(List<MeetupModel> meetups);

    List<MeetupAdminDto> meetupsToMeetupAdminDtos(List<MeetupModel> meetups);
}
