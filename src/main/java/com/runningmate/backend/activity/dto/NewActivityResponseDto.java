package com.runningmate.backend.activity.dto;

import com.runningmate.backend.activity.Activity;
import com.runningmate.backend.member.dto.MemberDto;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewActivityResponseDto {
    private Long id;
    private String title;
    private Double distance;
    private Integer hour;
    private Integer minute;
    private Integer altitude;
    private String imageUrl;

    public static NewActivityResponseDto fromEntity(Activity activity) {
        return NewActivityResponseDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .distance(activity.getDistance())
                .hour(activity.getHour())
                .minute(activity.getMinute())
                .altitude(activity.getAltitude())
                .imageUrl(activity.getImage_url())
                .build();
    }
}
