package com.runningmate.backend.route.controller;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import com.runningmate.backend.member.dto.MemberRouteDto;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.route.Route;
import com.runningmate.backend.route.dto.*;
import com.runningmate.backend.route.service.RouteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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
                                                                @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
                                                                @RequestParam(name = "searchTerm", defaultValue = "")
                                                                    @Size(max = 50) String searchTerm) {
        RouteListPaginationResponseDto routeResponseDtoList = routeService.getRoutesWithinRadius(latitude, longitude, radius, orderBy, page, searchTerm);
        return routeResponseDtoList;
    }

    @PostMapping("/savelists")
    @ResponseStatus(HttpStatus.CREATED)
    public RouteSaveListResponseDto createRouteSaveList(@RequestBody RouteSaveListRequestDto request, @RequestParam String username) {
        return routeService.createNewRouteSaveList(request, username);
    }

    @DeleteMapping("/savelists/{listId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRouteSaveList(@PathVariable Long listId, @RequestParam String username) {
        routeService.deleteRouteSaveList(listId, username);
    }

    @GetMapping("/savelists")
    @ResponseStatus(HttpStatus.OK)
    public List<RouteSaveListResponseDto> getRouteSaveLists(@RequestParam String username) {
        return routeService.getAllRouteSaveLists(username);
    }

    @GetMapping("/savelists/{listId}")
    @ResponseStatus(HttpStatus.OK)
    public RouteSaveListResponseDto getRouteSaveList(@PathVariable Long listId, @RequestParam String username) {
        return routeService.getRouteSaveList(listId, username);
    }

    @PostMapping("/savelists/{listId}/routes/{routeId}/save")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberRouteDto saveRouteToList(@PathVariable Long routeId, @PathVariable Long listId, @RequestParam String username) {
        return routeService.saveRouteToList(routeId, username, listId);
    }

    @DeleteMapping("/savelists/{listId}/routes/{routeId}/unsave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsaveRouteFromList(@PathVariable Long routeId, @PathVariable Long listId, @RequestParam String username) {
        routeService.unsaveRouteFromList(routeId, username, listId);
    }

    @PutMapping("/savelists/{listId}/public")
    @ResponseStatus(HttpStatus.OK)
    public RouteSaveListResponseDto makeRouteSaveListPublic(@PathVariable Long listId, @RequestParam String username) {
        return routeService.makeRouteSaveListPublic(listId, username);
    }

    @PutMapping("/savelists/{listId}/private")
    @ResponseStatus(HttpStatus.OK)
    public RouteSaveListResponseDto makeRouteSaveListPrivate(@PathVariable Long listId, @RequestParam String username) {
        return routeService.makeRouteSaveListPrivate(listId, username);
    }

}
