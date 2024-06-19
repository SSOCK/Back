package com.runningmate.backend.member.repository;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRouteRepository extends JpaRepository<MemberRoute, UUID> {
    List<MemberRoute> findByMemberAndSavedTrue(Member member);
    List<MemberRoute> findByMemberAndLikedTrue(Member member);
    Optional<MemberRoute> findByMemberIdAndRouteId(Long memberId, Long routeId);
}
