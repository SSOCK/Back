package com.runningmate.backend.club.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.runningmate.backend.club.Club;
import com.runningmate.backend.club.ClubMemberEntity;
import com.runningmate.backend.club.ClubRole;
import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.route.dto.CoordinateDto;
import com.runningmate.backend.schedule.Schedule;
import com.runningmate.backend.utils.PointCreator;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Map;
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
    private String locationName;
    private CoordinateDto locationCoordinate;
    private String profilePic;
    private String backgroundPic;
    private List<MemberDto> clubOwners;
    private List<MemberDto> clubModerators;
    private List<MemberDto> clubMembers;
    private List<ClubScheduleEntityResponseDto> schedules;

    public static ClubResponseDto fromEntity(Club club) {
        Map<ClubRole, List<MemberDto>> groupedMembers = club.getMembers().stream()
                .collect(Collectors.groupingBy(
                        ClubMemberEntity::getRole,
                        Collectors.mapping(clubMember -> MemberDto.fromEntity(clubMember.getMember()), Collectors.toList())
                ));

        List<MemberDto> owners = groupedMembers.getOrDefault(ClubRole.OWNER, List.of());
        List<MemberDto> moderators = groupedMembers.getOrDefault(ClubRole.MODERATOR, List.of());
        List<MemberDto> members = groupedMembers.getOrDefault(ClubRole.MEMBER, List.of());

        return ClubResponseDto.builder()
                .id(club.getId())
                .title(club.getTitle())
                .description(club.getDescription())
                .locationName(club.getLocationName())
                .locationCoordinate(PointCreator.toCoordinateDto(club.getLocationCoordinate()))
                .profilePic(club.getProfile_pic())
                .backgroundPic(club.getBackground_pic())
                .clubOwners(owners)
                .clubModerators(moderators)
                .clubMembers(members)
                .schedules(club.getSchedules().stream()
                        .map(clubScheduleEntity -> ClubScheduleEntityResponseDto.fromEntity(clubScheduleEntity))
                        .collect(Collectors.toList()))
                .build();
    }
}