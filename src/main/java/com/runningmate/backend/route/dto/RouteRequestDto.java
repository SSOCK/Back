package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.route.Route;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RouteRequestDto {

    @NotBlank
    @JsonProperty("course")
    private List<CoordinateDto> route;

    @NotBlank(message = "Distance Field has to be set.")
    @Digits(integer = 3, fraction = 2, message = "Up to 3 digits and 2 decimal places. Example: 128.11")
    private Double distance;

    @NotBlank(message = "Title must be given.")
    private String title;

    @NotBlank(message = "Time must be given")
    @Pattern(regexp = "^(2[0-4]|[01]?[0-9]):[0-5][0-9]$|^(2[0-4]|[01]?[0-9])$", message = "Invalid time format. HH:MM, H:MM, M, MM")
    private String time;

    @NotBlank(message = "Choose difficulty between 0(EASY), 1(MEDIUM), 2(HARD)")
    @Size(min = 0, max = 2, message = "Difficulty must be 0(EASY), 1(MEDIUM), 2(HARD)")
    private Integer difficulty;

    public Route toEntity(Member member, LineString path) {
        Route route = Route.builder()
                .member(member)
                .path(path)
                .difficulty(difficulty)
                .time(time)
                .title(title)
                .distance(distance)
                .build();
        return route;
    }
}
