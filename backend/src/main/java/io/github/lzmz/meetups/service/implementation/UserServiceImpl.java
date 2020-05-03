package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.mapper.UserMapper;
import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.model.UserModel;
import io.github.lzmz.meetups.repository.UserRepository;
import io.github.lzmz.meetups.security.Role;
import io.github.lzmz.meetups.service.UserService;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> findAll(Role role) {
        UserModel userExample = new UserModel();
        userExample.setRole(role);
        List<UserModel> users = userRepository.findAll(Example.of(userExample));
        return userMapper.usersToUserDtos(users);
    }

    @Override
    public UserDto create(SignUpDto signUpDto) throws DuplicateEntityException {
        UserModel user = userMapper.signUpDtoToUser(signUpDto);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEntityException(UserModel.class, signUpDto.getEmail(), "email");
        }

        if (signUpDto.isAdmin()) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }
}
