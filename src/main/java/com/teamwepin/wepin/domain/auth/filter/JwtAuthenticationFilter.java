package com.teamwepin.wepin.domain.auth.filter;

import com.teamwepin.wepin.domain.auth.support.AuthConstants;
import com.teamwepin.wepin.domain.jwt.application.JwtService;
import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import com.teamwepin.wepin.domain.jwt.exception.JwtError;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.entity.User;
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
    private final UserRepository userRepository;

    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/api/v1/users/join",
                            "/api/v1/login"
                    ));
    private static final List<String> SWAGGER_PREFIX_WHITELIST =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/v3/api-docs",
                            "/swagger-ui"
                    ));


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[AUTH] token 기반 인증 시도");
        String accessToken = jwtService.getTokenFromRequest(request, AuthConstants.ACCESS_TOKEN_HEADER);
        String refreshToken = jwtService.getTokenFromRequest(request, AuthConstants.REFRESH_TOKEN_HEADER);
        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        if (jwtService.validateToken(accessToken)) {
            log.info("[AUTH][TOKEN] 유효한 access token");
            String username = jwtService.getPayload(accessToken);
            log.info("[AUTH][TOKEN] username : {}", username);
            if (jwtService.validateToken(refreshToken)) {
                log.info("[AUTH][TOKEN] 유효한 refresh token");
                log.info("[AUTH][TOKEN] 정상 처리");

            } else {
                log.info("[AUTH][TOKEN] 만료된 refresh token. 재발급.");
                String newRefreshToken = jwtService.createRefreshToken(username);
                response.addHeader(AuthConstants.REFRESH_TOKEN_HEADER, AuthConstants.TOKEN_PREFIX + newRefreshToken);
            }
        } else {
            log.info("[AUTH][TOKEN] 만료된 access token.");
            if (jwtService.validateToken(refreshToken)) {
                log.info("[AUTH][TOKEN] 유효한 refresh token");
                log.info("[AUTH][TOKEN] refresh token을 검증하여 access token 재발급.");
                String username = jwtService.getPayload(refreshToken);
                // user db 조회하여 refresh token 검증.
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new CustomJwtException(JwtError.JWT_REFRESH_NOT_VALID));
                if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) { // user.getRefreshToken() null check 필요.
                    log.info("[AUTH][TOKEN] refresh token 불일치.");
                    throw new CustomJwtException(JwtError.JWT_REFRESH_NOT_VALID);
                }
                String newAccessToken = jwtService.createAccessToken(username);
                response.addHeader(AuthConstants.ACCESS_TOKEN_HEADER, AuthConstants.TOKEN_PREFIX + newAccessToken);
            } else {
                log.info("[AUTH][TOKEN] 만료된 refresh token.");
                log.info("[AUTH][TOKEN] 재로그인하여 accessToken, refreshToken 재발급 필요.");
                throw new CustomJwtException(JwtError.JWT_BOTH_TOKEN_EXPIRED);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()))
                || SWAGGER_PREFIX_WHITELIST.stream().anyMatch(excludePrefix -> request.getServletPath().startsWith(excludePrefix));
    }

}
