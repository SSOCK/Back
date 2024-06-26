package com.runningmate.backend.member.dto;

import com.runningmate.backend.member.MemberRoute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRouteDto {
    private Long id;
    private Long routeId;
    private String memberUsername;
    private String routeTitle;

    public MemberRouteDto(MemberRoute memberRoute) {
        this.id = memberRoute.getId();
        this.routeId = memberRoute.getRoute().getId();
        this.memberUsername = memberRoute.getMember().getUsername();
        this.routeTitle = memberRoute.getRoute().getTitle();
    }
}