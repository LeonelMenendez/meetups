package com.santander.meetup.service.implementation;

import com.santander.meetup.dto.request.SignInDTO;
import com.santander.meetup.dto.request.SignUpDTO;
import com.santander.meetup.dto.response.UserDTO;
import com.santander.meetup.exceptions.DuplicateEntityException;
import com.santander.meetup.security.JwtUtil;
import com.santander.meetup.service.AuthService;
import com.santander.meetup.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDTO signUp(SignUpDTO signUpDTO) throws DuplicateEntityException {
        return userService.create(signUpDTO);
    }

    @Override
    public UserDTO signIn(SignInDTO signInDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword());
        try {
            UserDetails userDetails = (UserDetails) authenticationManager.authenticate(authenticationToken).getPrincipal();
            UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);
            userDTO.setToken(jwtUtil.generateToken(userDetails));
            return userDTO;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("The email or password is incorrect");
        }
    }
}
