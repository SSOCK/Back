package com.runningmate.backend.schedule;

import com.runningmate.backend.entity.BaseTimeEntity;
import com.runningmate.backend.schedule.dto.CreateOrUpdateScheduleRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Table(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;

    public void update(CreateOrUpdateScheduleRequestDto updateDto) {
        this.name = updateDto.getTitle();
        this.description = updateDto.getDescription();
        this.dateTime = updateDto.getDateTime();
        this.location = updateDto.getLocation();
    }
}
