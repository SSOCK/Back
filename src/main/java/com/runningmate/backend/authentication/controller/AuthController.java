package com.runningmate.backend.authentication.controller;

import com.runningmate.backend.authentication.service.AuthService;
import com.runningmate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/valid/user/{username}")
    public Map<String, Boolean> usernameExists(@PathVariable("username") String username) {
        Boolean exists = authService.usernameExists(username);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);
        return result;
    }

    private final MemberService memberService;

}
