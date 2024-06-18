package com.runningmate.backend.route.repository;

import com.runningmate.backend.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query(value = "SELECT * FROM route r WHERE " +
            "ST_DWithin(r.path, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radius)", nativeQuery = true)
    List<Route> findRoutesWithinRadius(@Param("latitude") double latitude,
                                       @Param("longitude") double longitude,
                                       @Param("radius") int radius);
}
