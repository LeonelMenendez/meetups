package io.github.lzmz.meetups.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityResponder securityResponder;

    public JwtAccessDeniedHandler(SecurityResponder securityResponder) {
        this.securityResponder = securityResponder;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        securityResponder.accesDenied(req, res);
    }
}
