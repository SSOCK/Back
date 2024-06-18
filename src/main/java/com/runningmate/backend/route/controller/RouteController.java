package com.runningmate.backend.route.controller;

import com.runningmate.backend.route.dto.RouteRequestDto;
import com.runningmate.backend.route.dto.RouteResponseDto;
import com.runningmate.backend.route.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RouteResponseDto createRoute(@Valid @RequestBody RouteRequestDto routeRequest, @AuthenticationPrincipal UserDetails userDetails) throws BadRequestException {
        String username = userDetails.getUsername();
        return routeService.saveRoute(routeRequest, username);
    }

    @GetMapping("/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public RouteResponseDto getRouteById(@PathVariable(name = "routeId") Long routeId, @AuthenticationPrincipal UserDetails userDetails) {
        RouteResponseDto routeResponseDto = routeService.getRouteById(routeId);
        return routeResponseDto;
    }
}
