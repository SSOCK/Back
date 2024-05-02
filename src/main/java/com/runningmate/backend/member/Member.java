package com.runningmate.backend.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Table(name = "member")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    //https://cobbybb.tistory.com/14

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;//아이디

    @Column
    private String password;//비밀번호

    @Column(nullable = false, length = 30, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "Need to be valid email")
    private String email;//이메일

    @Column(nullable = false, length = 30)
    private String name;//이름

    @Enumerated(EnumType.STRING)
    private Role role;//권한 -> USER, ADMIN

    @Column(length = 1000)
    private String refreshtoken;

    public void updateRefreshToken(String token) {
        this.refreshtoken = token;
    }

    public void removeRefreshToken() {
        this.refreshtoken = null;
    }
}
