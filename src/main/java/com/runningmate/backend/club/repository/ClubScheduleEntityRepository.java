package com.runningmate.backend.club.repository;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClubScheduleEntityRepository extends JpaRepository<ClubScheduleEntity, Long> {
    List<ClubScheduleEntity> findByClubId(UUID clubId);
}
