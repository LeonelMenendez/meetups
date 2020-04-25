package com.santander.meetup.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityNotFoundException extends CustomException {

    /**
     * The name of the entity by which the exception was thrown.
     */
    private final String entityName;

    /**
     * The id by which the entity was tried to be find.
     */
    private final Object id;

    public EntityNotFoundException(Class<?> entityClass, Object id) {
        this.entityName = getEntityName(entityClass);
        this.id = id;
    }
}