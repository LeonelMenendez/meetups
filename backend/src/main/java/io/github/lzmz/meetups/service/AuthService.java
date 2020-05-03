package io.github.lzmz.meetups.service;

import io.github.lzmz.meetups.dto.request.SignInDto;
import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;

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
