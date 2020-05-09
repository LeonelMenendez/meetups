package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.EnrollmentCreationDto;
import io.github.lzmz.meetups.dto.response.EnrollmentDto;
import io.github.lzmz.meetups.model.EnrollmentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "meetup.id", target = "meetupId")
    @Mapping(source = "meetup.owner.name", target = "meetupOwnerName")
    @Mapping(source = "meetup.owner.email", target = "meetupOwnerEmail")
    @Mapping(source = "meetup.day", target = "meetupDay")
    @Mapping(source = "meetup.temperature", target = "meetupTemperature")
    EnrollmentDto enrollmentToEnrollmentDto(EnrollmentModel enrollment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "meetup", ignore = true)
    @Mapping(target = "checkedIn", ignore = true)
    EnrollmentModel enrollmentCreationDtoToEnrollment(EnrollmentCreationDto enrollmentCreationDto);

    List<EnrollmentDto> enrollmentsToEnrollmentDtos(List<EnrollmentModel> enrollments);
}
