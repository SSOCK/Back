package com.runningmate.backend.exception;

public class InvalidFileTypeException extends RuntimeException{
    public InvalidFileTypeException(String fileType) {
        super("Invalid File Type Given: " + fileType);
    }

    public InvalidFileTypeException(String fileType, String extension) {
        super("File type of " + fileType + " was given but file's extension is " + extension);
    }
}
