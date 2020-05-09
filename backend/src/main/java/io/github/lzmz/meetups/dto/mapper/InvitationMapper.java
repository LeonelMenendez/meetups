package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.InvitationCreationDto;
import io.github.lzmz.meetups.dto.response.InvitationDto;
import io.github.lzmz.meetups.model.InvitationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "meetup.id", target = "meetupId")
    @Mapping(source = "meetup.owner.name", target = "meetupOwnerName")
    @Mapping(source = "meetup.owner.email", target = "meetupOwnerEmail")
    @Mapping(source = "meetup.day", target = "meetupDay")
    @Mapping(source = "meetup.temperature", target = "meetupTemperature")
    InvitationDto invitationToInvitationDto(InvitationModel invitation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "meetup", ignore = true)
    @Mapping(target = "status", ignore = true)
    InvitationModel invitationCreationDtoToInvitation(InvitationCreationDto invitationCreationDto);

    List<InvitationDto> invitationsToInvitationDtos(List<InvitationModel> invitations);
}
