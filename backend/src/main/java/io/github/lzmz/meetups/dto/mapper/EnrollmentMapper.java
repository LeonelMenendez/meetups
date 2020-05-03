package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.EnrollmentCreationDto;
import io.github.lzmz.meetups.dto.response.EnrollmentDto;
import io.github.lzmz.meetups.model.EnrollmentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EnrollmentMapper {

    @Mapping(source = "enrollment.meetup.id", target = "meetupId")
    @Mapping(source = "enrollment.user.id", target = "userId")
    @Mapping(source = "enrollment.meetup.owner.name", target = "meetupOwnerName")
    @Mapping(source = "enrollment.meetup.owner.email", target = "meetupOwnerEmail")
    @Mapping(source = "enrollment.meetup.day", target = "meetupDay")
    @Mapping(source = "enrollment.meetup.temperature", target = "meetupTemperature")
    EnrollmentDto enrollmentToEnrollmentDto(EnrollmentModel enrollment);

    EnrollmentModel enrollmentCreationDtoToEnrollment(EnrollmentCreationDto enrollmentCreationDto);

    List<EnrollmentDto> enrollmentsToEnrollmentDtos(List<EnrollmentModel> enrollments);
}
