package com.runningmate.backend.route;

import com.runningmate.backend.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.LineString;

import java.math.BigDecimal;

@Entity
@Table(name = "route")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer difficulty;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private Double distance;

    @Column(columnDefinition = "geography(LineString, 4326)")
    private LineString path;
}