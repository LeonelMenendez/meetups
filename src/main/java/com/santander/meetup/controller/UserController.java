package com.santander.meetup.controller;

import com.santander.meetup.dto.request.SignInDTO;
import com.santander.meetup.dto.request.SignUpDTO;
import com.santander.meetup.dto.response.UserDTO;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Users")
@RestController
@RequestMapping(value = EndPoint.USERS)
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper mapper;

    /**
     * Signs up a new user.
     *
     * @param signUpDTO the sign up request body.
     * @return the signed up user.
     * @throws DuplicateEntityException if the user already exists.
     */
    @ApiOperation(value = "Signs up a new user")
    @PostMapping(EndPoint.SIGN_UP)
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody SignUpDTO signUpDTO) throws DuplicateEntityException {
        return new ResponseEntity<>(userService.signUp(signUpDTO), HttpStatus.CREATED);
    }

    /**
     * Signs in a user.
     *
     * @param signInDTO the sign in request body.
     * @return the signed in user.
     */
    @ApiOperation(value = "Signs in a user")
    @PostMapping(EndPoint.SIGN_IN)
    public ResponseEntity<UserDTO> signIn(@Valid @RequestBody SignInDTO signInDTO) {
        return new ResponseEntity<>(userService.signIn(signInDTO), HttpStatus.OK);
    }
}
