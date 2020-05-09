package io.github.lzmz.meetups.controller;

import io.github.lzmz.meetups.dto.request.SignInDto;
import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.endpoint.AuthEndpoint;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Authentication")
@RestController
@RequestMapping(value = AuthEndpoint.BASE)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Signs up a new user.
     *
     * @param signUpDTO the sign up request body.
     * @return the signed up user.
     * @throws DuplicateEntityException if the user already exists.
     */
    @Operation(summary = "Signs up a new user")
    @SecurityRequirements
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
    @Operation(summary = "Signs in a user")
    @SecurityRequirements
    @PostMapping(AuthEndpoint.SIGN_IN)
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody SignInDto signInDTO) {
        return new ResponseEntity<>(authService.signIn(signInDTO), HttpStatus.OK);
    }
}
