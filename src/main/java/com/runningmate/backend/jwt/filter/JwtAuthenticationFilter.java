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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final List<String> SKIP_ENDPOINTS = Arrays.asList("/login", "/signup", "/auth");
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        for (String prefix : SKIP_ENDPOINTS) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Separated refresh token check to a new api endpoint /auth/refresh.
        //Refresh token is stored in Cookie and sent only to /auth/refresh endpoint.
//        String refreshToken = jwtService.extractRefreshToken(request).filter(token -> jwtService.isTokenValid(token)).orElse(null);
//
//        if (refreshToken != null) {
//            jwtService.extractUsername(refreshToken).ifPresent(
//                    username -> memberRepository.findByUsername(username).ifPresent(
//                            member -> {
//                                if (member.getRefreshtoken() == refreshToken) {
//                                    String newAccessToken = jwtService.createAccessToken(username);
//                                    try {
//                                        jwtService.sendAccessToken(response, newAccessToken);
//                                    } catch (IOException e) {
//                                        throw new RuntimeException(e);
//                                    }
//                                }
//                            }
//                    )
//            );
//            return;
//        }

        jwtService.extractAccessToken(request).ifPresent(
                accessToken -> jwtService.extractUsername(accessToken).ifPresent(
                        username -> memberRepository.findByUsername(username).ifPresent(
                                member -> {
                                    UserDetails user = User.builder()
                                            .username(member.getUsername())
                                            .password(member.getPassword())
                                            .roles(member.getRole().name())
                                            .build();
                                    //TODO: Create Custom UserDetails to store member so i do not need to do another findByUsername in controller
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
