package com.runningmate.backend.authentication.service;


import com.runningmate.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public Boolean usernameExists(String username) {
        return memberRepository.existsByUsername(username);
    }

}
