package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.UserModel;
import com.santander.meetup.repository.UserRepository;
import com.santander.meetup.security.Role;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserModel findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(UserModel.class, id));
    }

    @Override
    public UserModel findByEmail(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(UserModel.class, email));
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
        return modelMapper.map(user, UserDto.class);
    }
}
