package com.runningmate.backend.route.service;

import com.runningmate.backend.exception.BadRequestException;
import com.runningmate.backend.exception.ExistsConflictException;
import com.runningmate.backend.exception.NoPermissionException;
import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.member.dto.MemberRouteDto;
import com.runningmate.backend.member.repository.MemberRouteRepository;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.route.Route;
import com.runningmate.backend.route.RouteSaveList;
import com.runningmate.backend.route.RouteSaveListMemberRoute;
import com.runningmate.backend.route.dto.*;
import com.runningmate.backend.route.repository.RouteRepository;
import com.runningmate.backend.route.repository.RouteSaveListMemberRouteRepository;
import com.runningmate.backend.route.repository.RouteSaveListRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final MemberRouteRepository memberRouteRepository;
    private final RouteSaveListMemberRouteRepository routeSaveListMemberRouteRepository;
    private final RouteSaveListRepository routeSaveListRepository;
    private final MemberService memberService;

    @Transactional
    public RouteResponseDto createRoute(RouteRequestDto request, String username) {
        List<CoordinateDto> coordinateDtos = request.getRoute();
        validateCoordinates(coordinateDtos);
        LineString lineString = coordinateDtoListToLineString(coordinateDtos);
        Route route = request.toEntity(memberService.getMemberByUsername(username), lineString);
        routeRepository.save(route);
        return new RouteResponseDto(route, coordinateDtos);
    }

    public RouteResponseDto getRouteById(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + routeId + " does not exist"));
        List<CoordinateDto> translatedCoordinates = lineStringToCoordinateDtoList(route.getPath());
        return new RouteResponseDto(route, translatedCoordinates);
    }

    public RouteListPaginationResponseDto getRoutesWithinRadius(double latitude, double longitude, int radius,
                                                                String orderBy, int page, String searchTerm) {
        validateCoordinate(new CoordinateDto(latitude, longitude));
        Pageable pageable = PageRequest.of(page, 10);
        String searchTermRemovedWS = searchTerm.trim();
        Page<Route> routes;
        switch (orderBy.toLowerCase()) {
            case "popularity":
                if (searchTermRemovedWS.isEmpty()) {
                    routes = routeRepository.findRoutesWithinRadiusOrderByPopularity(latitude, longitude, radius, pageable);
                } else {
                    routes = routeRepository.searchRoutesWithinRadiusOrderByPopularity(searchTermRemovedWS, latitude, longitude, radius, pageable);

                }
                break;
            default:
                //order by recent
                if (searchTermRemovedWS.isEmpty()) {
                    routes = routeRepository.findRoutesWithinRadiusOrderByCreatedAt(latitude, longitude, radius, pageable);
                } else {
                    routes = routeRepository.searchRoutesWithinRadiusOrderByCreatedAt(searchTermRemovedWS, latitude, longitude, radius, pageable);
                }
                break;
        }
        List<RouteResponseDto> routeDtos = changeRoutesToRouteResponseDtos(routes.getContent());
        System.out.println(routes.getTotalElements());
        return new RouteListPaginationResponseDto(routeDtos, routes.getTotalPages(), routes.getNumber());
    }

    @Transactional
    public RouteSaveListResponseDto createNewRouteSaveList(RouteSaveListRequestDto request, String username) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Check if user already has RouteSaveList with the same name
        boolean exists = routeSaveListRepository.existsByNameAndMember(request.getName(), member);
        if (exists) {
            throw new ExistsConflictException("A RouteSaveList with the name '" + request.getName() + "' already exists for this user.");
        }

        // Create a new RouteSaveList entity
        RouteSaveList routeSaveList = RouteSaveList.builder()
                .name(request.getName())
                .isPublic(request.isPublic())
                .member(member)
                .build();

        // Save the RouteSaveList entity to the database
        routeSaveListRepository.save(routeSaveList);

        // Return the response DTO
        return new RouteSaveListResponseDto(routeSaveList);
    }

    @Transactional(readOnly = true)
    public List<RouteSaveListResponseDto> getAllRouteSaveLists(String username) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Retrieve all RouteSaveList entities for the member
        List<RouteSaveList> routeSaveLists = routeSaveListRepository.findByMember(member);

        // Convert to DTOs and return
        return routeSaveLists.stream()
                .map(RouteSaveListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRouteSaveList(Long listId, String username) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Find the RouteSaveList by ID
        RouteSaveList routeSaveList = routeSaveListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveList with id " + listId + " does not exist"));

        // Check if the RouteSaveList belongs to the member
        if (!routeSaveList.getMember().getUsername().equals(username)) {
            throw new NoPermissionException("You do not have permission to delete this RouteSaveList");
        }

        // Delete the RouteSaveList
        routeSaveListRepository.delete(routeSaveList);
    }

    @Transactional(readOnly = true)
    public RouteSaveListResponseDto getRouteSaveList(Long listId, String username) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Find the RouteSaveList by ID
        RouteSaveList routeSaveList = routeSaveListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveList with id " + listId + " does not exist"));

        // Check if the RouteSaveList is public or RouteSaveList belongs to the member
        if (!routeSaveList.isPublic() && !routeSaveList.getMember().getUsername().equals(username)) {
            throw new NoPermissionException("You do not have permission to view this RouteSaveList");
        }

        // Return the DTO
        return new RouteSaveListResponseDto(routeSaveList, true);
    }

    @Transactional
    public MemberRouteDto saveRouteToList(Long routeId, String username, Long listId) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Find the route by ID
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route with id " + routeId + " does not exist"));

        // Find the RouteSaveList by ID
        RouteSaveList routeSaveList = routeSaveListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveList with id " + listId + " does not exist"));

        // Check if the RouteSaveList belongs to the member
        if (!routeSaveList.getMember().getUsername().equals(username)) {
            throw new NoPermissionException("You do not have permission to save a route to this list");
        }

        // Find or create the MemberRoute entity
        MemberRoute memberRoute = memberRouteRepository.findByMemberAndRoute(member, route)
                .orElseGet(() -> MemberRoute.builder()
                        .member(member)
                        .route(route)
                        .build());

        // Save the MemberRoute entity if it's new to avoid TransientObjectException
        if (memberRoute.getId() == null) {
            memberRoute = memberRouteRepository.save(memberRoute);
        }

        // Check if the association already exists
        boolean alreadyExists = routeSaveListMemberRouteRepository.existsByRouteSaveListAndMemberRoute(routeSaveList, memberRoute);
        if (alreadyExists) {
            throw new ExistsConflictException("Route is already saved in the list");
        }

        // Create and save the RouteSaveListMemberRoute entity
        RouteSaveListMemberRoute routeSaveListMemberRoute = RouteSaveListMemberRoute.builder()
                .routeSaveList(routeSaveList)
                .memberRoute(memberRoute)
                .build();
        routeSaveListMemberRouteRepository.save(routeSaveListMemberRoute);

        MemberRoute memberRouteCreated = memberRouteRepository.save(memberRoute);

        return new MemberRouteDto(memberRouteCreated);
    }

    @Transactional
    public void unsaveRouteFromList(Long routeId, String username, Long listId) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Find the route by ID
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route with id " + routeId + " does not exist"));

        // Find the RouteSaveList by ID
        RouteSaveList routeSaveList = routeSaveListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveList with id " + listId + " does not exist"));

        // Check if the RouteSaveList belongs to the member
        if (!routeSaveList.getMember().getUsername().equals(username)) {
            throw new NoPermissionException("You do not have permission to save a route to this list");
        }

        // Find the MemberRoute entity
        MemberRoute memberRoute = memberRouteRepository.findByMemberAndRoute(member, route)
                .orElseThrow(() -> new ResourceNotFoundException("MemberRoute with route id " + routeId + " for member " + member.getUsername() + " does not exist"));

        // Find the RouteSaveListMemberRoute entity
        RouteSaveListMemberRoute routeSaveListMemberRoute = routeSaveListMemberRouteRepository.findByRouteSaveListAndMemberRoute(routeSaveList, memberRoute)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveListMemberRoute with route id " + routeId + " in list id " + listId + " does not exist"));

        // Delete the RouteSaveListMemberRoute entity
        routeSaveListMemberRouteRepository.delete(routeSaveListMemberRoute);
    }

    @Transactional
    public RouteSaveListResponseDto makeRouteSaveListPublic(Long listId, String username) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Find the RouteSaveList by ID
        RouteSaveList routeSaveList = routeSaveListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveList with id " + listId + " does not exist"));

        // Check if the RouteSaveList belongs to the member
        if (!routeSaveList.getMember().getUsername().equals(username)) {
            throw new NoPermissionException("You do not have permission to make this RouteSaveList public");
        }

        // Set the RouteSaveList to public
        routeSaveList.setPublic(true);

        // Save the RouteSaveList entity
        routeSaveListRepository.save(routeSaveList);

        // Return the DTO
        return new RouteSaveListResponseDto(routeSaveList);
    }

    @Transactional
    public RouteSaveListResponseDto makeRouteSaveListPrivate(Long listId, String username) {
        // Get the member by username
        Member member = memberService.getMemberByUsername(username);

        // Find the RouteSaveList by ID
        RouteSaveList routeSaveList = routeSaveListRepository.findById(listId)
                .orElseThrow(() -> new ResourceNotFoundException("RouteSaveList with id " + listId + " does not exist"));

        // Check if the RouteSaveList belongs to the member
        if (!routeSaveList.getMember().getUsername().equals(username)) {
            throw new NoPermissionException("You do not have permission to make this RouteSaveList private");
        }

        // Set the RouteSaveList to private
        routeSaveList.setPublic(false);

        // Save the RouteSaveList entity
        routeSaveListRepository.save(routeSaveList);

        // Return the DTO
        return new RouteSaveListResponseDto(routeSaveList);
    }

    public List<RouteResponseDto> changeRoutesToRouteResponseDtos(List<Route> routes) {
        List<RouteResponseDto> routeDtos = new ArrayList<>();
        for (Route route: routes) {
            List<CoordinateDto> translatedCoordinates = lineStringToCoordinateDtoList(route.getPath());
            routeDtos.add(new RouteResponseDto(route, translatedCoordinates));
        }
        return routeDtos;
    }

    private LineString coordinateDtoListToLineString(List<CoordinateDto> coordinateDtos) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate[] coordinates = coordinateDtos.stream()
                .map(dto -> new Coordinate(dto.getLongitude(), dto.getLatitude()))
                .toArray(Coordinate[]::new);
        LineString lineString = geometryFactory.createLineString(coordinates);
        return lineString;
    }

    private List<CoordinateDto> lineStringToCoordinateDtoList(LineString lineString) {
        List<CoordinateDto> coordinateDtos = new ArrayList<>();
        for (Coordinate coordinate : lineString.getCoordinates()) {
            CoordinateDto dto = new CoordinateDto();
            dto.setLatitude(coordinate.getY());
            dto.setLongitude(coordinate.getX());
            coordinateDtos.add(dto);
        }
        return coordinateDtos;
    }

    private void validateCoordinate(CoordinateDto coordinateDto) {
        if (coordinateDto.getLatitude() < -90 || coordinateDto.getLatitude() > 90) {
            throw new BadRequestException("Invalid latitude: " + coordinateDto.getLatitude() + "\n Latitude must be within -90 and 90");
        }
        if (coordinateDto.getLongitude() < -180 || coordinateDto.getLongitude() > 180) {
            throw new BadRequestException("Invalid longitude: " + coordinateDto.getLongitude() + "\n Longitude must be within -180 and 180");
        }
    }

    private void validateCoordinates(List<CoordinateDto> coordinateDTOs) {
        for (CoordinateDto dto : coordinateDTOs) {
            validateCoordinate(dto);
        }
    }
}
