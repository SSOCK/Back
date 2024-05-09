package com.runningmate.backend.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.runningmate.backend.jwt.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken(username);
        jwtService.updateRefreshToken(username, refreshToken);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
//        Map<String, Object> map = new HashMap<>();
//        map.put("message", username + " login success!");
//        String json = objectMapper.writeValueAsString(map);
        response.setContentType("application/json");
//        response.getWriter().write(json);

    }
}
