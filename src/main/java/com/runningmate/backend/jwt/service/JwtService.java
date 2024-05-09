package com.runningmate.backend.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.runningmate.backend.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService implements JwtServiceInterface {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.access.header}")
    private String accessTokenHeader;
    @Value("${jwt.access.expiration}")
    private long refreshTokenExpiration;
    @Value("${jwt.refresh.header}")
    private String refreshTokenHeader;



    private static final String ACCESS_TOKEN_SUBJECT = "Access Token";

    private static final String REFRESH_TOKEN_SUBJECT = "Refresh Token";
    private static final String USERNAME_CLAIM = "username";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public String createAccessToken(String username) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        try {
            String token = JWT.create()
                    .withSubject(ACCESS_TOKEN_SUBJECT)
                    .withClaim(USERNAME_CLAIM, username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + (accessTokenExpiration * 1000)))
                    .sign(algorithm);

            return token;
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating the JWT: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String createRefreshToken(String username) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        try {
            String token = JWT.create()
                    .withSubject(REFRESH_TOKEN_SUBJECT)
                    .withClaim(USERNAME_CLAIM, username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + (refreshTokenExpiration * 1000)))
                    .sign(algorithm);

            return token;
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating the JWT: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateRefreshToken(String username, String refreshToken) {
        memberRepository.findByUsername(username)
                .ifPresentOrElse(member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("Member Not Found"));
    }

    @Override
    public void removeRefreshToken(String username) {
        memberRepository.findByUsername(username)
                .ifPresentOrElse(member -> member.removeRefreshToken(),
                        () -> new Exception("Member Not Found"));
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        //get header with name {accessTokenHeader} and remove the starting String "Bearer "
        return Optional.ofNullable(request.getHeader(accessTokenHeader)).filter(

                accessToken -> accessToken.startsWith(BEARER)

        ).map(accessToken -> accessToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshTokenHeader)).filter(

                refreshToken -> refreshToken.startsWith(BEARER)

        ).map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractUsername(String accessToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return Optional.ofNullable(verifier.verify(accessToken).getClaim(USERNAME_CLAIM).asString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        ObjectNode tokenJson = objectMapper.createObjectNode();
        tokenJson.put("access-token", accessToken)
                .put("refresh-token", refreshToken);

        response.getWriter().write(objectMapper.writeValueAsString(tokenJson));

    }

    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        ObjectNode tokenJson = objectMapper.createObjectNode();
        tokenJson.put("access-token", accessToken);

        response.getWriter().write(objectMapper.writeValueAsString(tokenJson));
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
