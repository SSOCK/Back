package com.runningmate.backend.route;

import com.runningmate.backend.entity.BaseTimeEntity;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "route")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "route")
    @Builder.Default
    private List<MemberRoute> memberRoutes = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String details;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(nullable = false)
    private Double distance;

    @Column(columnDefinition = "geography(LineString, 4326)")
    private LineString path;
}