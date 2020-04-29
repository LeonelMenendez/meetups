package com.santander.meetup.service;

import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.exceptions.EntityNotFoundException;
import com.santander.meetup.model.UserModel;
import com.santander.meetup.security.Role;

import java.util.List;

public interface UserService {

    /**
     * Finds a user by id.
     *
     * @param id the id to be found.
     * @return a user with the given id.
     * @throws EntityNotFoundException if the user wasn't found.
     */
    UserModel findById(Long id) throws EntityNotFoundException;

    /**
     * Finds a user by email.
     *
     * @param email the email to be found.
     * @return a user with the given email.
     * @throws EntityNotFoundException if the user wasn't found.
     */
    UserModel findByEmail(String email) throws EntityNotFoundException;

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