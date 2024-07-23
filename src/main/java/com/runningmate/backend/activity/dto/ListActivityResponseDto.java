package com.runningmate.backend.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.runningmate.backend.activity.dto.NewActivityResponseDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListActivityResponseDto {
    private List<NewActivityResponseDto> data;
}
