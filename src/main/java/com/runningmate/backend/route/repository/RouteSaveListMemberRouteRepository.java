package com.runningmate.backend.route.repository;

import com.runningmate.backend.member.MemberRoute;
import com.runningmate.backend.route.RouteSaveList;
import com.runningmate.backend.route.RouteSaveListMemberRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteSaveListMemberRouteRepository extends JpaRepository<RouteSaveListMemberRoute, Long> {
    boolean existsByRouteSaveListAndMemberRoute(RouteSaveList routeSaveList, MemberRoute memberRoute);

    Optional<RouteSaveListMemberRoute> findByRouteSaveListAndMemberRoute(RouteSaveList routeSaveList, MemberRoute memberRoute);
}
