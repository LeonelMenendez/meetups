package com.santander.meetup.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Class to handle API errors.
 */
@Data
public class ApiError implements Serializable {

    /**
     * An internal code of the error.
     */
    private int code;

    /**
     * The operation call status.
     */
    private int status;

    /**
     * The {@code LocalDateTime} instance of when the error happened. It is established
     * by default when a new {@code ApiError} is created.
     */
    private LocalDateTime timestamp;

    /**
     * User-friendly message about the error.
     */
    private String message;

    /**
     * List of detailed errors.
     */
    private List<String> errors;

    public ApiError(int code, HttpStatus status, String message, List<String> errors) {
        super();
        this.code = code;
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.errors = errors;
    }

    public ApiError(int code, HttpStatus status, String message, String error) {
        super();
        this.code = code;
        this.status = status.value();
        this.timestamp = LocalDateTime.now();
        this.message = message;
        errors = Arrays.asList(error);
    }
}
