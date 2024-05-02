package com.runningmate.backend.jwt.filter;

import com.runningmate.backend.jwt.service.JwtService;
import com.runningmate.backend.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.extractRefreshToken(request).filter(token -> jwtService.isTokenValid(token)).orElse(null);

        if (refreshToken != null) {
            jwtService.extractUsername(refreshToken).ifPresent(
                    username -> memberRepository.findByUsername(username).ifPresent(
                            member -> {
                                if (member.getRefreshtoken() == refreshToken) {
                                    String newAccessToken = jwtService.createAccessToken(username);
                                    jwtService.sendAccessToken(response, newAccessToken);
                                }
                            }
                    )
            );
            return;
        }

        jwtService.extractAccessToken(request).ifPresent(
                accessToken -> jwtService.extractUsername(accessToken).ifPresent(
                        username -> memberRepository.findByUsername(username).ifPresent(
                                member -> {
                                    UserDetails user = User.builder()
                                            .username(member.getUsername())
                                            .password(member.getPassword())
                                            .roles(member.getRole().name())
                                            .build();
                                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authoritiesMapper.mapAuthorities(user.getAuthorities()));
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                }
                        )
                )
        );
        System.out.println("Went through extracting username from accessToken");
        filterChain.doFilter(request, response);
    }
}
