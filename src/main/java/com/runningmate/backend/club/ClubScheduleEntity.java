package com.runningmate.backend.club;

import com.runningmate.backend.club.dto.CreateOrUpdateClubScheduleRequestDto;
import com.runningmate.backend.schedule.Schedule;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "club_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public void update(CreateOrUpdateClubScheduleRequestDto updateDto) {
        this.schedule.update(updateDto.getCreateOrUpdateScheduleRequestDto());
    }
}
