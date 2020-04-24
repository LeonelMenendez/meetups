package com.santander.meetup.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.meetup.exceptions.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, "Access denied", "You don't have permission to access this resource");
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = res.getWriter();
        out.print(mapper.writeValueAsString(apiError));
        out.flush();
    }
}
