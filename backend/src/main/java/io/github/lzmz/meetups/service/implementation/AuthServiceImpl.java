package io.github.lzmz.meetups.service.implementation;

import io.github.lzmz.meetups.dto.mapper.UserMapper;
import io.github.lzmz.meetups.dto.request.SignInDto;
import io.github.lzmz.meetups.dto.request.SignUpDto;
import io.github.lzmz.meetups.dto.response.UserDto;
import io.github.lzmz.meetups.exceptions.DuplicateEntityException;
import io.github.lzmz.meetups.model.UserModel;
import io.github.lzmz.meetups.security.JwtUtil;
import io.github.lzmz.meetups.service.AuthService;
import io.github.lzmz.meetups.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userMapper = userMapper;
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
            UserModel user = (UserModel) authenticationManager.authenticate(authenticationToken).getPrincipal();
            UserDto userDTO = userMapper.userToUserDto(user);
            userDTO.setToken(jwtUtil.generateToken(user));
            return userDTO;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("The email or password is incorrect");
        }
    }
}
