package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RouteListResponseDto {
    @JsonProperty("courses")
    private List<RouteResponseDto> routes;
}
