package com.runningmate.backend.schedule.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.runningmate.backend.route.dto.CoordinateDto;
import com.runningmate.backend.utils.PointCreator;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

/*
Sample Request JSON
{
    "title": "Weekly Club Meeting",
    "description": "Discuss the upcoming events and club activities.",
    "dateTime": "2024-07-25T10:00:00",
    "location": {
        "type": "Point",
        "coordinates": [102.0, 0.5]
    }
}
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOrUpdateScheduleRequestDto {
    @NotEmpty(message = "Title must not be empty or null")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotEmpty(message = "Detail must not be empty or null")
    @Size(max = 500, message = "Detail must not exceed 500 characters")
    private String description;

    @NotNull(message = "DateTime must not be null")
    @Future(message = "DateTime must be in the future")
    private LocalDateTime dateTime;

    @NotNull(message = "Location must not be null")
    private CoordinateDto location;

    public Point getLocation() {
        return PointCreator.createPoint(this.location.getLongitude(), this.location.getLatitude());
    }
}
