package com.runningmate.backend.club.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.runningmate.backend.club.Club;
import com.runningmate.backend.route.dto.CoordinateDto;
import com.runningmate.backend.utils.PointCreator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubRequestDto {
    @NotEmpty(message = "Title must not be empty or null")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Detail must not be empty or null")
    @Size(max = 500, message = "Detail must not exceed 500 characters")
    private String description;

    @NotNull(message = "Location must not be null")
    private CoordinateDto location;

    public Club toEntity() {
        return Club.builder()
                .title(this.title)
                .description(this.description)
                .location(PointCreator.createPoint(this.location.getLongitude(), this.location.getLatitude()))
                .build();
    }
}