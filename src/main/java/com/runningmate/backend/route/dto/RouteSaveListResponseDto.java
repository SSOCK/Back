package com.runningmate.backend.route.dto;

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
    private String memberUsername;
    private boolean isPublic;
    private int routeCount;
    private List<MemberRouteDto> memberRoutes;


    public RouteSaveListResponseDto(RouteSaveList routeSaveList) {
        this.id = routeSaveList.getId();
        this.name = routeSaveList.getName();
        this.memberUsername = routeSaveList.getMember().getUsername();
        this.isPublic = routeSaveList.isPublic();
        this.routeCount = routeSaveList.getRouteSaveListMemberRoutes().size();
    }

    public RouteSaveListResponseDto(RouteSaveList routeSaveList, boolean includeMemberRoutes) {
        this.id = routeSaveList.getId();
        this.name = routeSaveList.getName();
        this.memberUsername = routeSaveList.getMember().getUsername();
        this.routeCount = routeSaveList.getRouteSaveListMemberRoutes().size();
        if (includeMemberRoutes) {
            this.memberRoutes = routeSaveList.getRouteSaveListMemberRoutes().stream()
                    .map(routeSaveListMemberRoute -> new MemberRouteDto(routeSaveListMemberRoute.getMemberRoute()))
                    .collect(Collectors.toList());
        }
    }
}
