package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.route.Route;
import jakarta.validation.constraints.*;
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

    @NotEmpty(message = "Course details must be given")
    @JsonProperty("course")
    private List<CoordinateDto> route;

    @NotNull(message = "Distance Field has to be set.")
    @Max(value = 300, message = "Distance can't be larger than 300km")
    private Double distance;

    @NotBlank(message = "Title must be given.")
    private String title;

    //Remove time for now. Might add again later
//    @NotBlank(message = "Time must be given")
//    @Pattern(regexp = "^(2[0-4]|[01]?[0-9]):[0-5][0-9]$|^(2[0-4]|[01]?[0-9])$", message = "Invalid time format. HH:MM, H:MM, M, MM")
//    private String time;

    @NotNull(message = "Choose difficulty between 0(EASY), 1(MEDIUM), 2(HARD)")
    @Min(value = 0, message = "Difficulty must be between 0 and 2")
    @Max(value = 2, message = "Difficulty must be between 0 and 2")
    private Integer difficulty;

    public Route toEntity(Member member, LineString path) {
        Route route = Route.builder()
                .member(member)
                .path(path)
                .difficulty(difficulty)
//                .time(time)
                .title(title)
                .distance(distance)
                .build();
        return route;
    }
}
