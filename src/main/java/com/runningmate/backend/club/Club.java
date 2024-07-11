package com.runningmate.backend.club;

import com.runningmate.backend.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.*;

//게시판, 일정, 멤버(클럽장, 부클럽장등등 역할), 순위, 채팅방, 위치, 배경사진, 클럽 사진,
@Entity
@Getter
@Table(name = "club")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Club {
    public static final String DEFAULT_BACKGROUND_PIC = "https://storage.googleapis.com/runningmate-bucket/Screenshot%202024-06-20%20at%203.46.14%E2%80%AFPM.png";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "club_id")
    private UUID id;

    private String title; //TODO: updateTitle method
    private String detail; //TODO: updateDetail method

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;

    @Builder.Default
    @Column(nullable = false)
    private String profile_pic = Member.DEFAULT_PROFILE_PIC;

    @Builder.Default
    @Column(nullable = false)
    private String background_pic = Club.DEFAULT_BACKGROUND_PIC;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ClubMemberEntity> members = new HashSet<>();

//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<ClubPostEntity> posts = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ClubScheduleEntity> clubs = new ArrayList<>();
}
