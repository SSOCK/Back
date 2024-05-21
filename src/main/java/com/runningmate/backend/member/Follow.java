package com.runningmate.backend.member;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "follow")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_member_id")
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "following_member_id")
    private Member following;
}
