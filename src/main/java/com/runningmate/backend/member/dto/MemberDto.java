package com.runningmate.backend.member.dto;

import com.runningmate.backend.member.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {
    private Long id;
    private String username;
    private String email;
    private String name;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }
}
