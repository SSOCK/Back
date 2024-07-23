package com.runningmate.backend.club;

import com.runningmate.backend.entity.BaseTimeEntity;
import com.runningmate.backend.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.*;

@Entity
@Getter
@Table(name = "club")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Club extends BaseTimeEntity {
    public static final String DEFAULT_BACKGROUND_PIC = "https://storage.googleapis.com/runningmate-bucket/Screenshot%202024-06-20%20at%203.46.14%E2%80%AFPM.png";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "club_id")
    private UUID id;

    private String title; //TODO: updateTitle method

    private String description; //TODO: updateDetail method

    private String locationName;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point locationCoordinate;

    @Builder.Default
    @Column(nullable = false)
    private String profile_pic = Member.DEFAULT_PROFILE_PIC;

    @Builder.Default
    @Column(nullable = false)
    private String background_pic = Club.DEFAULT_BACKGROUND_PIC;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ClubMemberEntity> members = new ArrayList<>();

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<ClubPostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ClubScheduleEntity> schedules = new ArrayList<>();

    public void updateProfilePic(String url) {
        this.profile_pic = url;
    }

    public void updateBackgroundPic(String url) {
        this.background_pic = url;
    }

    public void addMember(ClubMemberEntity clubMemberEntity) {
        this.members.add(clubMemberEntity);
    }

    public void removeMember(ClubMemberEntity clubMemberEntity) { this.members.remove(clubMemberEntity);}

    public void addClubSchedule(ClubScheduleEntity clubScheduleEntity) { this.schedules.add(clubScheduleEntity);}

    public void removeClubSchedule(ClubScheduleEntity clubScheduleEntity) { this.schedules.remove(clubScheduleEntity);}

    public void updateTitle(String title) { this.title = title;}

    public void updateDescription(String description) { this.description = description;}
}
