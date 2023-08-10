package com.teamwepin.wepin.domain.auth.filter;

import com.teamwepin.wepin.domain.auth.support.AuthConstants;
import com.teamwepin.wepin.domain.jwt.application.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/api/v1/users/join",
                            "/api/v1/login"
                    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[AUTH] token 기반 인증 시도");
        String accessToken = jwtService.getTokenFromRequest(request, AuthConstants.ACCESS_TOKEN_HEADER);
        String refreshToken = jwtService.getTokenFromRequest(request, AuthConstants.REFRESH_TOKEN_HEADER);
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        //일단 access token만 해보자.
        if (jwtService.validateToken(accessToken)) {
            log.info("[AUTH][TOKEN] 유효한 access token");
            String payload = jwtService.getPayload(accessToken);
            log.info("[AUTH][TOKEN] username : {}", payload);

            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }

}
