package com.runningmate.backend.authentication.controller;

import com.runningmate.backend.authentication.dto.AccessTokenReissueDto;
import com.runningmate.backend.authentication.service.AuthService;
import com.runningmate.backend.exception.InvalidTokenException;
import com.runningmate.backend.jwt.service.JwtService;
import com.runningmate.backend.member.Member;
import com.runningmate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final MemberService memberService;

    @GetMapping("/valid/user/{username}")
    public Map<String, Boolean> usernameExists(@PathVariable("username") String username) {
        Boolean exists = authService.usernameExists(username);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);
        return result;
    }

    @PostMapping("/refresh")
    public AccessTokenReissueDto reissueAccessToken(@CookieValue(value = "refresh-token", defaultValue = "") String refreshToken) throws InvalidTokenException{
        if (!refreshToken.isEmpty() && jwtService.isTokenValid(refreshToken)) {
            Optional<String> usernameOpt = jwtService.extractUsername(refreshToken);
            if (usernameOpt.isPresent()) {
                String username = usernameOpt.get();
                Member member = memberService.getMemberByUsername(username);
                if (member.getRefreshtoken().equals(refreshToken)) {
                    String newAccessToken = jwtService.createAccessToken(username);
                    AccessTokenReissueDto accessTokenReissueDto = new AccessTokenReissueDto(newAccessToken);
                    return accessTokenReissueDto;
                }
            }
        }

        throw new InvalidTokenException();
    }
}
