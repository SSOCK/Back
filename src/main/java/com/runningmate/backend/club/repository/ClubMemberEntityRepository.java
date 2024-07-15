package com.runningmate.backend.club.repository;

import com.runningmate.backend.club.ClubMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberEntityRepository extends JpaRepository<ClubMemberEntity, Long> {
}
