package com.runningmate.backend.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.dto.MemberRouteDto;
import com.runningmate.backend.route.RouteSaveList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RouteSaveListResponseDto {
    private Long id;
    private String name;
    private Long listOwnerId;
    private String listOwnerUsername;
    private boolean isPublic;
    @JsonProperty("courseCount")
    private int routeCount;
    @JsonProperty("savedCourses")
    private List<MemberRouteDto> memberRoutes;


    public RouteSaveListResponseDto(RouteSaveList routeSaveList) {
        Member owner = routeSaveList.getMember();

        this.id = routeSaveList.getId();
        this.name = routeSaveList.getName();
        this.listOwnerUsername = owner.getUsername();
        this.listOwnerId = owner.getId();
        this.isPublic = routeSaveList.isPublic();
        this.routeCount = routeSaveList.getRouteSaveListMemberRoutes().size();
    }

    public RouteSaveListResponseDto(RouteSaveList routeSaveList, boolean includeMemberRoutes) {
        Member owner = routeSaveList.getMember();

        this.id = routeSaveList.getId();
        this.name = routeSaveList.getName();
        this.listOwnerUsername = owner.getUsername();
        this.listOwnerId = owner.getId();
        this.routeCount = routeSaveList.getRouteSaveListMemberRoutes().size();
        if (includeMemberRoutes) {
            this.memberRoutes = routeSaveList.getRouteSaveListMemberRoutes().stream()
                    .map(routeSaveListMemberRoute -> new MemberRouteDto(routeSaveListMemberRoute.getMemberRoute()))
                    .collect(Collectors.toList());
        }
    }
}
