package io.github.lzmz.meetups.dto.mapper;

import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "token", ignore = true)
    UserDto userToUserDto(UserModel user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "meetups", ignore = true)
    @Mapping(target = "createdMeetups", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    UserModel signUpDtoToUser(SignUpDto signUpDto);

    List<UserDto> usersToUserDtos(List<UserModel> users);
}
