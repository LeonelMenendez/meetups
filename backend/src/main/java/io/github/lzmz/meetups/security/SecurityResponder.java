package io.github.lzmz.meetups.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lzmz.meetups.exceptions.ApiError;
import io.github.lzmz.meetups.exceptions.ApiErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecurityResponder {

    private final ObjectMapper objectMapper;

    public SecurityResponder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void accesDenied(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN, "Access denied", "You don't have permission to access this resource");
        respond(req, res, apiError);
    }

    public void jwtUnexpected(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_UNEXPECTED, HttpStatus.UNAUTHORIZED, "Invalid JWT", "Missing or invalid JWT");
        respond(req, res, apiError);
    }

    public void jwtExpired(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_EXPIRED, HttpStatus.UNAUTHORIZED, "Expired JWT", "The JWT has expired and a new authentication is required");
        respond(req, res, apiError);
    }

    public void jwtUnsupported(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_UNSUPPORTED, HttpStatus.UNAUTHORIZED, "Unsupported JWT", "The JWT received is in a particular format/configuration that does not match the format expected by the application");
        respond(req, res, apiError);
    }

    public void jwtMalformed(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_MALFORMED, HttpStatus.UNAUTHORIZED, "Malformed JWT", "JWT was not correctly constructed");
        respond(req, res, apiError);
    }

    private void respond(HttpServletRequest req, HttpServletResponse res, ApiError apiError) throws IOException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(apiError.getStatus());
        PrintWriter out = res.getWriter();
        out.print(objectMapper.writeValueAsString(apiError));
        out.flush();
    }
}
