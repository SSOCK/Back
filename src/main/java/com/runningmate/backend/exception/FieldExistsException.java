package com.runningmate.backend.exception;

import java.util.List;

public class FieldExistsException extends RuntimeException {
    private List<String> exists;

    public FieldExistsException(List<String> exists) {
        super("Fields already exist: " + exists);
        this.exists = exists;
    }

    public List<String> getExists() {
        return exists;
    }
}
