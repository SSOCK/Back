package com.runningmate.backend.authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenReissueDto {
    @JsonProperty("access-token")
    private String accessToken;
}
