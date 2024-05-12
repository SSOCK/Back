package com.runningmate.backend.authentication.controller;

import com.runningmate.backend.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
