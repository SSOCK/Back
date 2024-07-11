package com.runningmate.backend.schedule.service;

import com.runningmate.backend.schedule.Schedule;
import com.runningmate.backend.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule createSchedule(String name, LocalDateTime dateTime, Point location) {
        Schedule schedule = Schedule.builder()
                .name(name)
                .dateTime(dateTime)
                .location(location)
                .build();
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Optional<Schedule> getSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    @Transactional
    public Schedule updateSchedule(Long id, String name, LocalDateTime dateTime, Point location) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        schedule.update(name, dateTime, location);  // Ensure Schedule entity has an update method
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
