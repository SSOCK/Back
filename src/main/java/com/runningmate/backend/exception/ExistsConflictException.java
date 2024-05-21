package com.runningmate.backend.exception;

public class ExistsConflictException extends RuntimeException {
    public ExistsConflictException(String message) {
        super(message);
    }
}