package com.santander.meetup.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.meetup.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "Invalid JWT token", "Missing or invalid JWT token");
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = res.getWriter();
        out.print(mapper.writeValueAsString(apiError));
        out.flush();
    }
}
