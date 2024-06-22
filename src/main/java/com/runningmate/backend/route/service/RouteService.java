package com.runningmate.backend.route.service;

import com.runningmate.backend.exception.BadRequestException;
import com.runningmate.backend.exception.ResourceNotFoundException;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.member.repository.MemberRouteRepository;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.route.Route;
import com.runningmate.backend.route.dto.*;
import com.runningmate.backend.route.repository.RouteRepository;
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

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final MemberRouteRepository memberRouteRepository;
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

    public List<Route> getSavedRoutes(Member member) {
        List<MemberRoute> memberRoutes = memberRouteRepository.findByMemberAndSavedTrue(member);
        List<Route> routes = new ArrayList<>();
        for (MemberRoute memberRoute: memberRoutes) {
            routes.add(memberRoute.getRoute());
        }
        return routes;
    }

    @Transactional
    public MemberRoute saveRoute(Long routeId, Member member) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Route with id " + routeId + " does not exist.")
                );
        Optional<MemberRoute> memberRouteOptional = memberRouteRepository.findByMemberIdAndRouteId(member.getId(), routeId);
        MemberRoute memberRoute = memberRouteOptional.orElseGet(() -> MemberRoute.builder()
                .route(route)
                .member(member)
                .build());

        memberRoute.saveRoute();
        return memberRouteRepository.save(memberRoute);
    }

    @Transactional
    public void unsaveRoute(Long routeId, Member member) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Route with id " + routeId + " does not exist.")
                );
        Optional<MemberRoute> memberRouteOptional = memberRouteRepository.findByMemberIdAndRouteId(member.getId(), routeId);
        if (memberRouteOptional.isPresent()) {
            MemberRoute memberRoute = memberRouteOptional.get();
            memberRoute.unsaveRoute();
            if (!memberRoute.isLiked() && !memberRoute.isSaved()) {
                memberRouteRepository.delete(memberRoute);
            } else {
                memberRouteRepository.save(memberRoute);
            }
        }
    }

    @Transactional
    public MemberRoute likeRoute(Long routeId, Member member) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route with id " + routeId + " does not exist."));
        Optional<MemberRoute> memberRouteOptional = memberRouteRepository.findByMemberIdAndRouteId(member.getId(), routeId);
        MemberRoute memberRoute = memberRouteOptional.orElseGet(() -> MemberRoute.builder()
                .route(route)
                .member(member)
                .build());
        memberRoute.likeRoute();
        return memberRouteRepository.save(memberRoute);
    }

    @Transactional
    public void unlikeRoute(Long routeId, Member member) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Route with id " + routeId + " does not exist.")
                );
        Optional<MemberRoute> memberRouteOptional = memberRouteRepository.findByMemberIdAndRouteId(member.getId(), routeId);
        if (memberRouteOptional.isPresent()) {
            MemberRoute memberRoute = memberRouteOptional.get();
            memberRoute.unlikeRoute();
            if (!memberRoute.isLiked() && !memberRoute.isSaved()) {
                memberRouteRepository.delete(memberRoute);
            } else {
                memberRouteRepository.save(memberRoute);
            }
        }
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
