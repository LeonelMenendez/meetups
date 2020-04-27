package com.santander.meetup.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * Class to handle not allowed values.
 */
@Getter
@Setter
public class ValueNotAllowedException extends CustomException {

    /**
     * The name of the attribute by which the exception was thrown.
     */
    private final String attribute;

    /**
     * The value that caused the exception.
     */
    private final Object value;

    /**
     * The cause by which the value is not allowed.
     */
    private final String reason;

    public ValueNotAllowedException(String attribute, Object value, String reason) {
        this.attribute = attribute;
        this.value = value;
        this.reason = reason;
    }
}