package com.runningmate.backend.club.repository;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubMemberEntity;
import com.runningmate.backend.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubMemberEntityRepository extends JpaRepository<ClubMemberEntity, Long> {
    Optional<ClubMemberEntity> findByClubAndMember(Club club, Member member);
}
