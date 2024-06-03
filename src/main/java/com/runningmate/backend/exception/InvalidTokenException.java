package com.runningmate.backend.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("Provided token is not valid");
    }
}
