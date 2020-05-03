package com.santander.meetup.service;

import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.security.Role;

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
     * Determines if a user already exists by the given email.
     *
     * @param email the email to be find.
     * @return {@code true} if the user exists. {@code false} otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Creates a new user.
     *
     * @param signUpDTO the user creation data.
     * @return the created user.
     * @throws DuplicateEntityException if the user already exists.
     */
    UserDto create(SignUpDto signUpDTO) throws DuplicateEntityException;
}