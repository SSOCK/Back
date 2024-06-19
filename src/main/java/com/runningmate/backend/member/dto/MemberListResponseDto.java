package com.runningmate.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class MemberListResponseDto {
    private String type;
    private List<MemberDto> members;
}
