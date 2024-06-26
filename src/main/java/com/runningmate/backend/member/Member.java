package com.runningmate.backend.member;

import com.runningmate.backend.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "member")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {
    public static final String DEFAULT_PROFILE_PIC = "https://storage.googleapis.com/runningmate-bucket/Screenshot%202024-06-20%20at%203.46.14%E2%80%AFPM.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;//아이디

    @Column(nullable = false, length = 72)
    private String password;//비밀번호

    @Column(nullable = false, length = 50, unique = true)
    private String email;//이메일

    @Column(nullable = false, length = 30)
    private String name;//이름

    @Enumerated(EnumType.STRING)
    private Role role;//권한 -> USER, ADMIN

    @Column(length = 1000)
    private String refreshtoken;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Follow> followings = new ArrayList<>();

    @Column
    @Builder.Default
    private String profilePicture = DEFAULT_PROFILE_PIC;

    public void updateRefreshToken(String token) {
        this.refreshtoken = token;
    }

    public void removeRefreshToken() {
        this.refreshtoken = null;
    }

    public void updateProfilePicture(String url) {
        this.profilePicture = url;
    }
}
