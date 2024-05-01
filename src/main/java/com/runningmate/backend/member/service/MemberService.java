package com.runningmate.backend.member.service;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberSignupRequest;
import com.runningmate.backend.member.Role;
import com.runningmate.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Member signup(MemberSignupRequest memberSignupRequest) throws BadRequestException {
        if (memberRepository.existsByUsername(memberSignupRequest.getUsername())) {
            throw new BadRequestException("User already exists");
        }

        Member member = Member.builder()
                .email(memberSignupRequest.getEmail())
                .name(memberSignupRequest.getName())
                .username(memberSignupRequest.getUsername())
                .password(passwordEncoder.encode(memberSignupRequest.getPassword()))
                .role(Role.USER)
                .build();

        memberRepository.save(member);
        return member;
    }
}
