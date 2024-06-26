package com.runningmate.backend.exception;

public class NoPermissionException extends RuntimeException{
    public NoPermissionException() {
        super("User does not have permission to perform this command");
    }

    public NoPermissionException(String message) {
        super(message);
    }
}
