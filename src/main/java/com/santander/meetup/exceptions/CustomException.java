package com.santander.meetup.exceptions;

import lombok.Data;

/**
 * Base class to handle custom exceptions.
 */
@Data
public abstract class CustomException extends Exception {

    /**
     * Replaces the start of capitalized words with hyphens and removes the <i>Model</i> suffix.
     *
     * @param entityClass the class from where will be obtain the name of the entity..
     * @return the entity name.
     */
    public static String getEntityName(Class<?> entityClass) {
        String entityName = entityClass.getSimpleName();
        entityName = entityName.replaceAll("Model", "");
        entityName = entityName.replaceAll("([A-Z])", "-$1");
        entityName = entityName.replaceFirst("-([A-Z])", "$1");

        return entityName;
    }
}
