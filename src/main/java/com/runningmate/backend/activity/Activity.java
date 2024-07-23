package com.runningmate.backend.activity;

import com.runningmate.backend.entity.BaseTimeEntity;
import com.runningmate.backend.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "activity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Activity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Integer hour;

    @Column(nullable = false)
    private Integer minute;

    @Column(nullable = false)
    private Integer altitude;

    @Column(nullable = false)
    private String image_url;
}