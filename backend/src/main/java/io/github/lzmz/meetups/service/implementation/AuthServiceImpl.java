package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.request.SignInDto;
import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.security.JwtUtil;
import io.github.lzmz.meetups.service.AuthService;
import io.github.lzmz.meetups.service.UserService;
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
