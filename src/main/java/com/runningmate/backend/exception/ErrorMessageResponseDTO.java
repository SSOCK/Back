package com.runningmate.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorMessageResponseDTO {
    private String error;
}
