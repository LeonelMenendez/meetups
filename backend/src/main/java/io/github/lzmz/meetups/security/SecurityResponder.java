package io.github.lzmz.meetups.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lzmz.meetups.exceptions.ApiError;
import io.github.lzmz.meetups.exceptions.ApiErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SecurityResponder {

    private final ObjectMapper objectMapper;

    public SecurityResponder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles a JWT exception and sends the corresponding response.
     *
     * @param res {@link HttpServletResponse}
     * @param ex  the exception to handle.
     * @throws IOException if an input or output exception occurred.
     */
    public void handleJwtException(HttpServletResponse res, Exception ex) throws IOException {
        if (ex instanceof UnsupportedJwtException) {
            unsupportedJwt(res);
        } else if (ex instanceof MalformedJwtException) {
            malformedJwt(res);
        } else if (ex instanceof SignatureException) {
            invalidSignatureJwt(res);
        } else if (ex instanceof ExpiredJwtException) {
            expiredJwt(res);
        } else if (ex instanceof IllegalArgumentException) {
            illegalArgumentJwt(res);
        }
    }

    /**
     * Sends an access denied response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void accessDenied(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN, "Access denied", "You don't have permission to access this resource");
        respond(res, apiError);
    }

    /**
     * Sends an unsupported JWT response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void unsupportedJwt(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_UNSUPPORTED, HttpStatus.UNAUTHORIZED, "Unsupported JWT", "The JWT received is in a particular format/configuration that does not match the format expected by the application");
        respond(res, apiError);
    }

    /**
     * Sends a malformed JWT response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void malformedJwt(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_MALFORMED, HttpStatus.UNAUTHORIZED, "Malformed JWT", "JWT was not correctly constructed");
        respond(res, apiError);
    }

    /**
     * Sends an invalid signature JWT response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void invalidSignatureJwt(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_INVALID_SIGNATURE, HttpStatus.UNAUTHORIZED, "Invalid signature", "JWS signature validation fails");
        respond(res, apiError);
    }

    /**
     * Sends an expired JWT response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void expiredJwt(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_EXPIRED, HttpStatus.UNAUTHORIZED, "Expired JWT", "The JWT has expired and a new authentication is required");
        respond(res, apiError);
    }

    /**
     * Sends an illegal argument JWT response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void illegalArgumentJwt(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_ILLEGAL_ARGUMENT, HttpStatus.UNAUTHORIZED, "Invalid claims", "The claims string is null or empty or only whitespace");
        respond(res, apiError);
    }

    /**
     * Sends an unexpected JWT response.
     *
     * @param res {@link HttpServletResponse}
     * @throws IOException if an input or output exception occurred.
     * @see #respond(HttpServletResponse, ApiError)
     */
    public void unexpectedJwt(HttpServletResponse res) throws IOException {
        ApiError apiError = new ApiError(ApiErrorCode.JWT_UNEXPECTED, HttpStatus.UNAUTHORIZED, "Invalid JWT", "Missing or invalid JWT");
        respond(res, apiError);
    }

    /**
     * Sends a response based on the given {@link ApiError}.
     *
     * @param res      {@link HttpServletResponse}
     * @param apiError the API error on which the response will be based.
     * @throws IOException if an input or output exception occurred.
     */
    private void respond(HttpServletResponse res, ApiError apiError) throws IOException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(apiError.getStatus());
        PrintWriter out = res.getWriter();
        out.print(objectMapper.writeValueAsString(apiError));
        out.flush();
    }
}
