package com.runningmate.backend.route.service;

import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.member.service.MemberService;
import com.runningmate.backend.route.Route;
import com.runningmate.backend.route.dto.CoordinateDto;
import com.runningmate.backend.route.dto.RouteRequestDto;
import com.runningmate.backend.route.dto.RouteResponseDto;
import com.runningmate.backend.route.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final MemberService memberService;

    @Transactional
    public RouteResponseDto saveRoute(RouteRequestDto request, String username) {
        List<CoordinateDto> coordinateDtos = request.getRoute();
        validateCoordinates(coordinateDtos);
        LineString lineString = coordinateDtoListToLineString(coordinateDtos);
        Route route = request.toEntity(memberService.getMemberByUsername(username), lineString);
        routeRepository.save(route);
        return new RouteResponseDto(route, coordinateDtos);
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

    private void validateCoordinates(List<CoordinateDto> coordinateDTOs) {
        for (CoordinateDto dto : coordinateDTOs) {
            if (dto.getLatitude() < -90 || dto.getLatitude() > 90) {
                throw new IllegalArgumentException("Invalid latitude: " + dto.getLatitude());
            }
            if (dto.getLongitude() < -180 || dto.getLongitude() > 180) {
                throw new IllegalArgumentException("Invalid longitude: " + dto.getLongitude());
            }
        }
    }
}
