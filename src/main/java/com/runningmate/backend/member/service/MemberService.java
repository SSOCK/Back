package com.runningmate.backend.member.service;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberSignupRequest;
import com.runningmate.backend.member.Role;
import com.runningmate.backend.member.exception.FieldExistsException;
import com.runningmate.backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Member signup(MemberSignupRequest memberSignupRequest) throws FieldExistsException {
        List<String> exists = checkExistingFields(memberSignupRequest);

        if (!exists.isEmpty()) {
            throw new FieldExistsException(exists);
        }

        Member member = createMember(memberSignupRequest);

        memberRepository.save(member);
        return member;
    }

    private List<String> checkExistingFields(MemberSignupRequest request) {
        List<String> exists = new ArrayList<>();

        if (memberRepository.existsByEmail(request.getEmail())) {
            exists.add("email");
        }
        if (memberRepository.existsByUsername(request.getUsername())) {
            exists.add("username");
        }

        return exists;
    }

    private Member createMember(MemberSignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
    }

}
