package com.runningmate.backend.member.repository;

import com.runningmate.backend.member.Follow;
import com.runningmate.backend.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(Member follower);
    List<Follow> findByFollowing(Member following);
}
