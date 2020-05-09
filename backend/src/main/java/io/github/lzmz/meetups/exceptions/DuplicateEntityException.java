package io.github.lzmz.meetups.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * Class to handle duplicate entity exceptions.
 */
@Getter
@Setter
public class DuplicateEntityException extends CustomException {

    /**
     * The name of the entity by which the exception was thrown.
     */
    private final String entityName;

    /**
     * A list of values that caused the exception.
     */
    private final List<Object> values;

    /**
     * A list of unique fields by which the exception was thrown.
     */
    private final List<Object> uniqueFields;

    public DuplicateEntityException(Class<?> entityClass, List<Object> values, List<Object> uniqueFields) {
        this.entityName = getEntityName(entityClass);
        this.values = values;
        this.uniqueFields = uniqueFields;
    }

    public DuplicateEntityException(Class<?> entityClass, Object value, String field) {
        this.entityName = getEntityName(entityClass);
        this.values = Collections.singletonList(value);
        this.uniqueFields = Collections.singletonList(field);
    }
}