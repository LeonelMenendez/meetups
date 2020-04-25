package com.santander.meetup.service;

import com.santander.meetup.dto.request.SignUpDTO;
import com.santander.meetup.dto.response.UserDTO;
import com.santander.meetup.exceptions.DuplicateEntityException;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param signUpDTO the user creation data.
     * @return the created user.
     */
    UserDTO create(SignUpDTO signUpDTO) throws DuplicateEntityException;
}