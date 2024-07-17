package com.runningmate.backend.schedule.dto;

import com.runningmate.backend.route.dto.CoordinateDto;
import com.runningmate.backend.schedule.Schedule;
import com.runningmate.backend.utils.PointCreator;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleResponseDto {
    private String title;
    private String description;
    private CoordinateDto location;
    private LocalDateTime date;
    private LocalDateTime createdAt;

    public static ScheduleResponseDto fromEntity(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .title(schedule.getName())
                .description(schedule.getDescription())
                .location(PointCreator.toCoordinateDto(schedule.getLocation()))
                .date(schedule.getDateTime())
                .createdAt(schedule.getCreatedAt())
                .build();
    }
}
