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

/**
 * Class to handle errors appropriately to be sent as responses.
 */
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Triggered when an unsupported HTTP method for a given end point was received.
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
        ex.getSupportedHttpMethods().forEach(method -> error.append(method + " "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, message, error.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when an unsupported media type for a given end point was received.
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
        StringBuilder error = new StringBuilder();
        error.append(ex.getContentType());
        error.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(mediaType -> error.append(mediaType + ", "));

        ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, error.substring(0, error.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when an argument annotated with {@code @Valid} failed on being validated.
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
     * Triggered when a {@code required} request parameter was missing.
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
     * Triggered when a requested DML operation resulted in a violation of a defined integrity constraint.
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

        ApiError apiError = new ApiError(HttpStatus.CONFLICT, message.toString().trim(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when a method argument was not the expected type.
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
     * Triggered when a given entity was not found.
     *
     * @param ex the exception to handle.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        String message = ex.getEntityName() + " was not found";
        String error = ex.getEntityName() + " was not found for parameter " + ex.getId();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when a given entity already exists in the database.
     *
     * @param ex the exception to handle.
     * @return a {@code ResponseEntity} object with the error handled.
     */
    @ExceptionHandler({DuplicateEntityException.class})
    public ResponseEntity<Object> handleDuplicateResource(DuplicateEntityException ex) {
        String message = ex.getEntityName() + " already exists";
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append(ex.getValue() + " already exists. Select another ");
        ex.getUniqueFields().forEach(field -> errorBuilder.append(field + "-"));
        String error = errorBuilder.toString().replaceAll("-$", "");

        ApiError apiError = new ApiError(HttpStatus.CONFLICT, message, error);
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
