package com.runningmate.backend.member;

import com.runningmate.backend.route.Route;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "member_route")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    private boolean liked;

    private boolean saved;

    public void saveRoute() {
        this.saved = true;
    }

    public void unsaveRoute() {
        this.saved = false;
    }

    public void likeRoute() {
        this.liked = true;
    }

    public void unlikeRoute() {
        this.liked = false;
    }
}
