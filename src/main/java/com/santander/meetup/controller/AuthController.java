package com.santander.meetup.controller;

import com.santander.meetup.dto.request.SignInDto;
import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.endpoint.AuthEndpoint;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Authentication")
@RestController
@RequestMapping(value = AuthEndpoint.ROOT)
public class AuthController {

    @Autowired
    AuthService authService;

    /**
     * Signs up a new user.
     *
     * @param signUpDTO the sign up request body.
     * @return the signed up user.
     * @throws DuplicateEntityException if the user already exists.
     */
    @ApiOperation(value = "Signs up a new user")
    @PostMapping(AuthEndpoint.SIGN_UP)
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpDto signUpDTO) throws DuplicateEntityException {
        return new ResponseEntity<>(authService.signUp(signUpDTO), HttpStatus.CREATED);
    }

    /**
     * Signs in a user.
     *
     * @param signInDTO the sign in request body.
     * @return the signed in user.
     */
    @ApiOperation(value = "Signs in a user")
    @PostMapping(AuthEndpoint.SIGN_IN)
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody SignInDto signInDTO) {
        return new ResponseEntity<>(authService.signIn(signInDTO), HttpStatus.OK);
    }
}
