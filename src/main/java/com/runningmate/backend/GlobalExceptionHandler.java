package com.runningmate.backend;

import com.runningmate.backend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleFieldExistsException(FieldExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("exists", e.getExists()));
    }

    @ExceptionHandler(ExistsConflictException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleAlreadyFollowingException(ExistsConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleInvalidFileTypeException(InvalidFileTypeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
