package com.santander.meetup.service;

import com.santander.meetup.dto.request.SignInDTO;
import com.santander.meetup.dto.request.SignUpDTO;
import com.santander.meetup.dto.response.UserDTO;
import com.santander.meetup.exceptions.DuplicateEntityException;

public interface UserService {

    /**
     * Signs up a new user.
     *
     * @param signUpDTO the sign up data.
     * @return the signed up user.
     */
    UserDTO signUp(SignUpDTO signUpDTO) throws DuplicateEntityException;

    /**
     * Signs in a user.
     *
     * @param signInDTO the sign in data.
     * @return the signed in user.
     */
    UserDTO signIn(SignInDTO signInDTO);
}