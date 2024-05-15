package com.runningmate.backend;

import com.runningmate.backend.member.exception.FieldExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleFieldExistsException(FieldExistsException fe) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("exists", fe.getExists()));
    }
}
