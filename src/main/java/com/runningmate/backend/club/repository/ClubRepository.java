package com.runningmate.backend.club.repository;

import com.runningmate.backend.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClubRepository extends JpaRepository<Club, UUID> {
}
