package com.runningmate.backend.member.controller;

import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.MemberSignupRequest;
import com.runningmate.backend.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Object> userSignup(@Valid @RequestBody MemberSignupRequest memberSignupRequest) {
        Member newMember = memberService.signup(memberSignupRequest);
        return ResponseEntity.ok().body(memberSignupRequest.getUsername() + " successfully registered.");
    }

}
