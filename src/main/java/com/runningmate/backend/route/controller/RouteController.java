package com.runningmate.backend.route.controller;

import com.runningmate.backend.route.dto.RouteRequestDto;
import com.runningmate.backend.route.dto.RouteResponseDto;
import com.runningmate.backend.route.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @PostMapping
    public RouteResponseDto createRoute(@Valid @RequestBody RouteRequestDto routeRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return routeService.saveRoute(routeRequest, username);
    }

}
