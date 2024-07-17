package com.runningmate.backend.club.dto;

import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubMemberEntity;
import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.route.dto.CoordinateDto;
import com.runningmate.backend.schedule.Schedule;
import com.runningmate.backend.utils.PointCreator;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClubResponseDto {
    private UUID id;
    private String title;
    private String description;
    private CoordinateDto location;
    private String profilePic;
    private String backgroundPic;
    private List<MemberDto> clubMembers;
    private List<ClubScheduleEntityResponseDto> schedules;

    public static ClubResponseDto fromEntity(Club club) {
        return ClubResponseDto.builder()
                .id(club.getId())
                .title(club.getTitle())
                .description(club.getDescription())
                .location(PointCreator.toCoordinateDto(club.getLocation()))
                .profilePic(club.getProfile_pic())
                .backgroundPic(club.getBackground_pic())
                .clubMembers(club.getMembers().stream()
                        .map(ClubMemberEntity::getMember)
                        .map(MemberDto::fromEntity)
                        .collect(Collectors.toList()))
                .schedules(club.getSchedules().stream()
                        .map(clubScheduleEntity -> ClubScheduleEntityResponseDto.fromEntity(clubScheduleEntity))
                        .collect(Collectors.toList()))
                .build();
    }
}