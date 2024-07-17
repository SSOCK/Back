package com.runningmate.backend.club.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateTitleDescriptionRequestDto {
    @NotEmpty(message = "Title must not be empty or null")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Detail must not be empty or null")
    @Size(max = 500, message = "Detail must not exceed 500 characters")
    private String description;
}
