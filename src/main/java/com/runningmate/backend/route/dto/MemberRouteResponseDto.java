package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.MemberRoute;
import lombok.Getter;

@Getter
public class MemberRouteResponseDto {
    private Long memberId;
    @JsonProperty("courseId")
    private Long routeId;
    private boolean liked;
    private boolean saved;

    public MemberRouteResponseDto(MemberRoute memberRoute) {
        this.memberId = memberRoute.getMember().getId();
        this.routeId = memberRoute.getRoute().getId();
        this.liked = memberRoute.isLiked();
        this.saved = memberRoute.isSaved();
    }
}
