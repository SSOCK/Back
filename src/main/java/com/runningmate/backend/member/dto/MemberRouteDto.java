package com.runningmate.backend.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.MemberRoute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRouteDto {
    private Long id;
    @JsonProperty("courseId")
    private Long routeId;
    @JsonProperty("courseTitle")
    private String routeTitle;

    public MemberRouteDto(MemberRoute memberRoute) {
        this.id = memberRoute.getId();
        this.routeId = memberRoute.getRoute().getId();
        this.routeTitle = memberRoute.getRoute().getTitle();
    }
}