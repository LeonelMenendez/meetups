package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto userToUserDto(UserModel user);

    UserModel signUpDtoToUser(SignUpDto signUpDto);

    List<UserDto> usersToUserDtos(List<UserModel> users);
}
