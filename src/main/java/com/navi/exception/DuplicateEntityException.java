package com.navi.exception;

public class DuplicateEntityException extends Exception {
    public DuplicateEntityException(final String entityType, final String entityID) {
        super(String.format("Entity with the given ID already exists in the DB. entityType: %s, entityID: %s", entityType, entityID));
    }
}
