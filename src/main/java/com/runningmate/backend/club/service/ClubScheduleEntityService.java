package com.runningmate.backend.club.service;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubScheduleEntity;
import com.runningmate.backend.club.dto.CreateOrUpdateClubScheduleRequestDto;
import com.runningmate.backend.club.repository.ClubScheduleEntityRepository;
import com.runningmate.backend.schedule.Schedule;
import com.runningmate.backend.schedule.dto.CreateOrUpdateScheduleRequestDto;
import com.runningmate.backend.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClubScheduleEntityService {

    private final ClubScheduleEntityRepository clubScheduleEntityRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ClubScheduleEntity createClubSchedule(Club club, CreateOrUpdateClubScheduleRequestDto clubScheduleDto) {
        CreateOrUpdateScheduleRequestDto scheduleDto = clubScheduleDto.getCreateOrUpdateScheduleRequestDto();
        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .name(scheduleDto.getTitle())
                .description(scheduleDto.getDescription())
                .dateTime(scheduleDto.getDateTime())
                .location(scheduleDto.getLocation())
                .build());

        ClubScheduleEntity clubScheduleEntity = ClubScheduleEntity.builder()
                .club(club)
                .schedule(schedule)
                .build();
        return clubScheduleEntityRepository.save(clubScheduleEntity);
    }

    @Transactional(readOnly = true)
    public Optional<ClubScheduleEntity> getClubScheduleById(Long id) {
        return clubScheduleEntityRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ClubScheduleEntity> getClubSchedules(UUID clubId) {
        return clubScheduleEntityRepository.findByClubId(clubId);
    }

    @Transactional
    public void deleteClubSchedule(Long id) {
        clubScheduleEntityRepository.deleteById(id);
    }
}
