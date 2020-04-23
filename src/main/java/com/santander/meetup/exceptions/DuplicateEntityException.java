package com.santander.meetup.exceptions;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Class to handle duplicate entity exceptions.
 */
@Data
public class DuplicateEntityException extends CustomException {

    /**
     * The name of the entity by which the exception was thrown.
     */
    private final String entityName;

    /**
     * The value that caused the exception
     */
    private final Object value;

    /**
     * A list of unique fields by which the exception was thrown.
     */
    private final List<Object> uniqueFields;

    public DuplicateEntityException(Class<?> entityClass, Object value, List<Object> uniqueFields) {
        this.entityName = getEntityName(entityClass);
        this.value = value;
        this.uniqueFields = uniqueFields;
    }

    public DuplicateEntityException(Class<?> entityClass, Object value, String field) {
        this.entityName = getEntityName(entityClass);
        this.value = value;
        this.uniqueFields = Arrays.asList(field);
    }
}