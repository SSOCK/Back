package com.runningmate.backend.club.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.runningmate.backend.club.ClubScheduleEntity;
import com.runningmate.backend.schedule.dto.ScheduleResponseDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubScheduleEntityResponseDto {
    private Long id;
    @JsonUnwrapped
    private ScheduleResponseDto schedule;

    public static ClubScheduleEntityResponseDto fromEntity(ClubScheduleEntity clubScheduleEntity) {
        return ClubScheduleEntityResponseDto.builder()
                .id(clubScheduleEntity.getId())
                .schedule(ScheduleResponseDto.fromEntity(clubScheduleEntity.getSchedule()))
                .build();

    }
}
