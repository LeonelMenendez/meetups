package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.model.UserModel;
import com.santander.meetup.repository.UserRepository;
import com.santander.meetup.security.Role;
import com.santander.meetup.service.UserService;
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
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto create(SignUpDto signUpDTO) throws DuplicateEntityException {
        UserModel user = modelMapper.map(signUpDTO, UserModel.class);

        if (existsByEmail(user.getEmail())) {
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
