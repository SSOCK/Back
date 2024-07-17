package com.runningmate.backend.club.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageRequestDto {

    @Size(max = 2000, message = "The URL must not exceed 2000 characters")
    @NotEmpty(message = "The URL must not be empty")
    private String newImageUrl;
}
