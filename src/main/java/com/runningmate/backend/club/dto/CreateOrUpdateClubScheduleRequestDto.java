package com.runningmate.backend.club.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.runningmate.backend.schedule.dto.CreateOrUpdateScheduleRequestDto;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOrUpdateClubScheduleRequestDto{
    @JsonUnwrapped
    private CreateOrUpdateScheduleRequestDto createOrUpdateScheduleRequestDto;
}
