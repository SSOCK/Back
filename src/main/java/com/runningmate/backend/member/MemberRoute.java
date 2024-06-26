package com.runningmate.backend.member;

import com.runningmate.backend.route.Route;
import com.runningmate.backend.route.RouteSaveListMemberRoute;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "member_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "memberRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RouteSaveListMemberRoute> routeSaveListMemberRoutes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
