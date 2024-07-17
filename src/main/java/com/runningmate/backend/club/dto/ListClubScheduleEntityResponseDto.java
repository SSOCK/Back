package com.runningmate.backend.club.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ListClubScheduleEntityResponseDto {
    private List<ClubScheduleEntityResponseDto> schedules;
}
