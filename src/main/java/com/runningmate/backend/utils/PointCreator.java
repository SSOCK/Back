package com.runningmate.backend.utils;

import com.runningmate.backend.route.dto.CoordinateDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PointCreator {

    public static Point createPoint(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return geometryFactory.createPoint(coordinate);
    }

    public static CoordinateDto toCoordinateDto(Point point) {
        if (point == null) {
            return null;
        }
        return new CoordinateDto(point.getY(), point.getX());
    }
}