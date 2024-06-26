package com.runningmate.backend.route.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteSaveListRequestDto {
    @Size(min = 1, max = 50)
    private String name;
    private boolean isPublic;
}
