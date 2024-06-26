package com.runningmate.backend.route.repository;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.route.RouteSaveList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteSaveListRepository extends JpaRepository<RouteSaveList, Long> {
    List<RouteSaveList> findByMember(Member member);
}
