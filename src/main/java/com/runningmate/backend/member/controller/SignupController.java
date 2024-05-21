package com.runningmate.backend.member.controller;

import com.runningmate.backend.member.dto.MemberDto;
import com.runningmate.backend.member.dto.MemberSignupRequest;
import com.runningmate.backend.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Object> userSignup(@Valid @RequestBody MemberSignupRequest memberSignupRequest) {
        MemberDto newMember = memberService.signup(memberSignupRequest);
        return ResponseEntity.ok().body(newMember);
    }
}
