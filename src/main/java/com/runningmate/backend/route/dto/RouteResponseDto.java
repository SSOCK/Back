package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.route.Route;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RouteResponseDto {
    private Long id;
    private String title;
    private String details;
    private Double distance;
    private Integer difficulty;
//    private String time;
    private MemberDto member;
    @JsonProperty("course")
    private List<CoordinateDto> route;

    public RouteResponseDto(Route route, List<CoordinateDto> routes) {
        this.id = route.getId();
        this.member = MemberDto.fromEntity(route.getMember());
        this.distance = route.getDistance();
        this.difficulty = route.getDifficulty();
        this.details = route.getDetails();
//        this.time = route.getTime();
        this.title = route.getTitle();
        this.route = routes;
    }
}
