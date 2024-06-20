package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateDto {

    @JsonProperty("La")
    private double latitude;
    @JsonProperty("Ma")
    private double longitude;
}