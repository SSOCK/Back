package com.runningmate.backend.route;

import com.runningmate.backend.entity.BaseTimeEntity;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberRoute;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "route_save_list",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "member_id"})
        })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteSaveList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "routeSaveList", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RouteSaveListMemberRoute> routeSaveListMemberRoutes = new ArrayList<>();
}
