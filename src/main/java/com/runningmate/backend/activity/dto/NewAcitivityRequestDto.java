package com.runningmate.backend.activity.dto;

import com.runningmate.backend.activity.Activity;
import com.runningmate.backend.member.Member;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NewAcitivityRequestDto {

    @NotEmpty(message = "Title must not be empty or null")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotNull(message = "Distance must not be null")
    @Positive(message = "Distance must be positive")
    private Double distance;

    @NotNull(message = "Hour must not be null")
    @Min(value = 0, message = "Hour must be at least 0")
    @Max(value = 23, message = "Hour must not exceed 23")
    private Integer hour;

    @NotNull(message = "Minute must not be null")
    @Min(value = 0, message = "Minute must be at least 0")
    @Max(value = 59, message = "Minute must not exceed 59")
    private Integer minute;

    @NotNull(message = "Altitude must not be null")
    private Integer altitude;

    @NotEmpty(message = "Image URL must not be empty or null")
    @URL(message = "Image URL must be a valid URL")
    private String image_url;

    public Activity toEntity(Member member) {
        return Activity.builder()
                .member(member)
                .title(this.title)
                .distance(this.distance)
                .hour(this.hour)
                .minute(this.minute)
                .altitude(this.altitude)
                .image_url(this.image_url)
                .build();
    }
}
