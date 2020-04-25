package com.santander.meetup.service;

import com.santander.meetup.dto.request.SignInDto;
import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;

public interface AuthService {

    /**
     * Signs up a new user.
     *
     * @param signUpDTO the sign up data.
     * @return the signed up user.
     * @throws DuplicateEntityException if the user already exists.
     */
    UserDto signUp(SignUpDto signUpDTO) throws DuplicateEntityException;

    /**
     * Signs in a user.
     *
     * @param signInDTO the sign in data.
     * @return the signed in user.
     */
    UserDto signIn(SignInDto signInDTO);
}
