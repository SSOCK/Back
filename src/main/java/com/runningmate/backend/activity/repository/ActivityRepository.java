package com.runningmate.backend.activity.repository;

import com.runningmate.backend.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByMemberOrderByCreatedAtDesc();
}
