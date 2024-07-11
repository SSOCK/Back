package com.runningmate.backend.club.repository;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubScheduleEntityRepository extends JpaRepository<ClubScheduleEntity, Long> {
}
