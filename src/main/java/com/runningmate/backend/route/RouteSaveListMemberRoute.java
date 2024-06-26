package com.runningmate.backend.route;

import com.runningmate.backend.member.MemberRoute;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "route_save_list_member_route",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"route_save_list_id", "member_route_id"})}
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteSaveListMemberRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_save_list_id")
    private RouteSaveList routeSaveList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_route_id")
    private MemberRoute memberRoute;
}
