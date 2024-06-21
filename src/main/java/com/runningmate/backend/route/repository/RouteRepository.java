package com.runningmate.backend.route.repository;

import com.runningmate.backend.route.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    //Might be better to create a column in Route table to keep count of number of likes it has
    @Query(value = "SELECT r.* FROM route r " +
            "LEFT JOIN member_route mr ON r.id = mr.route_id " +
            "WHERE ST_DWithin(r.path, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius) " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(CASE WHEN mr.liked = true THEN 1 ELSE NULL END) DESC, r.created_at DESC",
            countQuery = "SELECT count(*) FROM route r " +
                    "WHERE ST_DWithin(r.path, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)",
            nativeQuery = true)
    Page<Route> findRoutesWithinRadiusOrderByPopularity(@Param("latitude") double latitude,
                                                        @Param("longitude") double longitude,
                                                        @Param("radius") int radius,
                                                        Pageable pageable);

    @Query(value = "SELECT * FROM route r WHERE " +
            "ST_DWithin(r.path, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius) " +
            "ORDER BY r.created_at DESC",
            countQuery = "SELECT count(*) FROM route r WHERE " +
                    "ST_DWithin(r.path, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)",
            nativeQuery = true)
    Page<Route> findRoutesWithinRadiusOrderByCreatedAt(@Param("latitude") double latitude,
                                                       @Param("longitude") double longitude,
                                                       @Param("radius") int radius,
                                                       Pageable pageable);
}
