package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.InvitationCreationDto;
import io.github.lzmz.meetups.dto.response.InvitationDto;
import io.github.lzmz.meetups.model.InvitationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvitationMapper {

    @Mapping(source = "invitation.meetup.id", target = "meetupId")
    @Mapping(source = "invitation.user.id", target = "userId")
    @Mapping(source = "invitation.meetup.owner.name", target = "meetupOwnerName")
    @Mapping(source = "invitation.meetup.owner.email", target = "meetupOwnerEmail")
    @Mapping(source = "invitation.meetup.day", target = "meetupDay")
    @Mapping(source = "invitation.meetup.temperature", target = "meetupTemperature")
    InvitationDto invitationToInvitationDto(InvitationModel invitation);

    InvitationModel invitationCreationDtoToInvitation(InvitationCreationDto invitationCreationDto);

    List<InvitationDto> invitationsToInvitationDtos(List<InvitationModel> invitations);
}
