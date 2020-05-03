package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.model.UserModel;
import io.github.lzmz.meetups.repository.UserRepository;
import io.github.lzmz.meetups.security.Role;
import io.github.lzmz.meetups.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDto> findAll(Role role) {
        UserModel userExample = new UserModel();
        userExample.setRole(role);
        List<UserModel> users = userRepository.findAll(Example.of(userExample));

        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(toDto(user)));
        return userDtos;
    }

    @Override
    public UserDto create(SignUpDto signUpDTO) throws DuplicateEntityException {
        UserModel user = modelMapper.map(signUpDTO, UserModel.class);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEntityException(UserModel.class, signUpDTO.getEmail(), "email");
        }

        if (signUpDTO.isAdmin()) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }

        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        userRepository.save(user);
        return toDto(user);
    }

    private UserDto toDto(UserModel user) {
        return modelMapper.map(user, UserDto.class);
    }
}
