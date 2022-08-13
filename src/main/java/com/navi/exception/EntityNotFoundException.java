package com.navi.exception;

/**
 * Thrown from a repository if an entity for a particular type is not found
 */
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(final String entityType, final String entityID) {
        super(String.format("Entity with the given ID is not found in DB. entityType: %s, entityID: %s", entityType, entityID));
    }
}
