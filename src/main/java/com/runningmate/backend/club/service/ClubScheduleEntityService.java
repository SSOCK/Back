package com.runningmate.backend.club.service;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubScheduleEntity;
import com.runningmate.backend.club.repository.ClubScheduleEntityRepository;
import com.runningmate.backend.schedule.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubScheduleEntityService {

    private final ClubScheduleEntityRepository clubScheduleEntityRepository;

    @Transactional
    public ClubScheduleEntity createClubSchedule(Club club, Schedule schedule) {
        ClubScheduleEntity clubScheduleEntity = ClubScheduleEntity.builder()
                .club(club)
                .schedule(schedule)
                .build();
        return clubScheduleEntityRepository.save(clubScheduleEntity);
    }

    @Transactional(readOnly = true)
    public Optional<ClubScheduleEntity> getClubSchedule(Long id) {
        return clubScheduleEntityRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ClubScheduleEntity> getAllClubSchedules() {
        return clubScheduleEntityRepository.findAll();
    }

    @Transactional
    public void deleteClubSchedule(Long id) {
        clubScheduleEntityRepository.deleteById(id);
    }
}
