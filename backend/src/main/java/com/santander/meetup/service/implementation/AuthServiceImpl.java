package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.SignInDto;
import com.santander.meetup.dto.request.SignUpDto;
import com.santander.meetup.dto.response.UserDto;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.security.JwtUtil;
import com.santander.meetup.service.AuthService;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService, ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDto signUp(SignUpDto signUpDTO) throws DuplicateEntityException {
        return userService.create(signUpDTO);
    }

    @Override
    public UserDto signIn(SignInDto signInDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword());
        try {
            UserDetails userDetails = (UserDetails) authenticationManager.authenticate(authenticationToken).getPrincipal();
            UserDto userDTO = modelMapper.map(userDetails, UserDto.class);
            userDTO.setToken(jwtUtil.generateToken(userDetails));
            return userDTO;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("The email or password is incorrect");
        }
    }
}
