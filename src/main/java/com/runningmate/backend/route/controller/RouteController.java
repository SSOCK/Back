package com.runningmate.backend.route.controller;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.route.Route;
import com.runningmate.backend.route.dto.*;
import com.runningmate.backend.route.service.RouteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RouteResponseDto createRoute(@Valid @RequestBody RouteRequestDto routeRequest, @AuthenticationPrincipal UserDetails userDetails) throws BadRequestException {
        String username = userDetails.getUsername();
        return routeService.createRoute(routeRequest, username);
    }

    @GetMapping("/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public RouteResponseDto getRouteById(@PathVariable(name = "routeId") Long routeId) {
        RouteResponseDto routeResponseDto = routeService.getRouteById(routeId);
        return routeResponseDto;
    }

    @GetMapping("/radius")
    @ResponseStatus(HttpStatus.OK)
    public RouteListPaginationResponseDto getRoutesWithinRadius(@RequestParam(name = "latitude") double latitude,
                                                                @RequestParam(name = "longitude") double longitude,
                                                                @RequestParam(name = "radius") @Max(5000) @Min(0) int radius,
                                                                @RequestParam(name = "orderBy", defaultValue = "recent") String orderBy,
                                                                @RequestParam(name = "page", defaultValue = "0") @Min(0) int page) {
        RouteListPaginationResponseDto routeResponseDtoList = routeService.getRoutesWithinRadius(latitude, longitude, radius, orderBy, page);
        return routeResponseDtoList;
    }

    @PostMapping("/{routeId}/save")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberRouteResponseDto saveRoute(@PathVariable(name = "routeId") Long routeId, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberByUsername(userDetails.getUsername());
        MemberRoute memberRoute = routeService.saveRoute(routeId, member);
        return new MemberRouteResponseDto(memberRoute);
    }

    @DeleteMapping("/{routeId}/unsave")
    @ResponseStatus(HttpStatus.OK) //Return 200 instead of 204 for uniformity
    public void unsaveRoute(@PathVariable(name = "routeId") Long routeId, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberByUsername(userDetails.getUsername());
        routeService.unsaveRoute(routeId, member);
    }

    @PostMapping("/{routeId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberRouteResponseDto likeRoute(@PathVariable(name = "routeId") Long routeId, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberByUsername(userDetails.getUsername());
        MemberRoute memberRoute = routeService.likeRoute(routeId, member);
        return new MemberRouteResponseDto(memberRoute);
    }

    @DeleteMapping("/{routeId}/unlike")
    @ResponseStatus(HttpStatus.OK) //Return 200 instead of 204 for uniformity
    public void unlikeRoute(@PathVariable(name = "routeId") Long routeId, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberByUsername(userDetails.getUsername());
        routeService.unlikeRoute(routeId, member);
    }

    @GetMapping("/saved")
    @ResponseStatus(HttpStatus.OK)
    public RouteListResponseDto getSavedRoutes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Member member = memberService.getMemberByUsername(username);
        List<Route> routes = routeService.getSavedRoutes(member);
        List<RouteResponseDto> routeResponseDtos = routeService.changeRoutesToRouteResponseDtos(routes);
        return new RouteListResponseDto(routeResponseDtos);
    }
}
