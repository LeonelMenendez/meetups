package io.github.lzmz.meetups.service;

import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.security.Role;

import java.util.List;

public interface UserService {

    /**
     * Finds all the users filtered by the given parameters.
     *
     * @param role the role that will be filtered.
     * @return a list of users filtered by the given parameters.
     */
    List<UserDto> findAll(Role role);

    /**
     * Creates a new user.
     *
     * @param signUpDTO the user creation data.
     * @return the created user.
     * @throws DuplicateEntityException if the user already exists.
     */
    UserDto create(SignUpDto signUpDTO) throws DuplicateEntityException;
}