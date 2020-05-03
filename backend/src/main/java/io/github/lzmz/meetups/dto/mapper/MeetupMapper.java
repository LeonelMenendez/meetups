package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.MeetupCreationDto;
import io.github.lzmz.meetups.dto.response.MeetupAdminDto;
import io.github.lzmz.meetups.dto.response.MeetupUserDto;
import io.github.lzmz.meetups.model.MeetupModel;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeetupMapper {

    @Mapping(source = "meetup.owner.id", target = "ownerId")
    @Mapping(source = "meetup.owner.name", target = "ownerName")
    @Mapping(source = "meetup.owner.email", target = "ownerEmail")
    MeetupUserDto meetupToMeetupUserDto(MeetupModel meetup);

    @InheritConfiguration
    MeetupAdminDto meetupToMeetupAdminDto(MeetupModel meetup);

    MeetupModel meetupCreationDtoToMeetup(MeetupCreationDto meetupCreationDto);

    List<MeetupUserDto> meetupsToMeetupUserDtos(List<MeetupModel> meetups);

    List<MeetupAdminDto> meetupsToMeetupAdminDtos(List<MeetupModel> meetups);
}
