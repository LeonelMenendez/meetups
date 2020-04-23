package com.santander.meetup.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Triggered when unsupported HTTP method for given end point is received.
     *
     * @param ex      the exception to handle.
     * @param headers {@code HttpHeaders}.
     * @param status  {@code HttpStatus}.
     * @param request {@code WebRequest}.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Unsupported HTTP method";
        StringBuilder error = new StringBuilder();
        error.append(ex.getMethod());
        error.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> error.append(t + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, message, error.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when unsupported media type for given end point is received.
     *
     * @param ex      the exception to handle.
     * @param headers {@code HttpHeaders}.
     * @param status  {@code HttpStatus}.
     * @param request {@code WebRequest}.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Unsupported media type";
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when argument annotated with {@code @Valid} failed validation.
     *
     * @param ex      the exception to handle.
     * @param headers {@code HttpHeaders}.
     * @param status  {@code HttpStatus}.
     * @param request {@code WebRequest}.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder message = new StringBuilder("Invalid arguments: ");
        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
            message.append(error.getField() + " ");
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
            message.append(error.getObjectName() + " ");
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message.toString().trim(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when a {@code required} request parameter is missing.
     *
     * @param headers {@code HttpHeaders}.
     * @param status  {@code HttpStatus}.
     * @param request {@code WebRequest}.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Parameter missing";
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when requested DML operation resulted in a violation of a defined integrity constraint.
     *
     * @param ex the exception to handle.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder("Invalid arguments: ");
        List<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            message.append(violation.getPropertyPath() + " ");
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message.toString().trim(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when method argument is not the expected type.
     *
     * @param ex the exception to handle.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid " + ex.getName() + "argument type";
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Default Handler. It deals with all other exceptions that don't have specific handlers.
     *
     * @param ex the exception to handle.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        String message = "It's not you. It's us. We are having some problems";
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, "error occurred");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}
