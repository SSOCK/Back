package com.runningmate.backend.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface JwtServiceInterface {

    String createAccessToken(String username);

    String createRefreshToken(String username);

    void updateRefreshToken(String username, String refreshToken);
    void removeRefreshToken(String username);

    Optional<String> extractAccessToken(HttpServletRequest request);
    Optional<String> extractRefreshToken(HttpServletRequest request);
    Optional<String> extractUsername(String accessToken);

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException;
    void sendAccessToken(HttpServletResponse response, String accessToken) throws IOException;

    boolean isTokenValid(String token);

}
