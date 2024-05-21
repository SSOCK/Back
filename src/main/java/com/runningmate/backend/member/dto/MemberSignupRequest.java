package com.runningmate.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignupRequest {
    @NotBlank(message = "username cannot be empty")
    @Pattern(regexp = "[ㄱ-힣a-zA-Z\\s\\d-_]+")
    @Size(min=3, max = 20)
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Size(max=50, message = "Email is too long")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 20, message = "Password must be at least 6 characters long")
    private String password;

    @Size(min = 1, max = 30)
    @Pattern(regexp = "[ㄱ-힣a-zA-Z\\s]+")
    private String name;
}
