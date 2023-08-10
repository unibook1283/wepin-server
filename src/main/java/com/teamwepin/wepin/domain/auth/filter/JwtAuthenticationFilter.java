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

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[AUTH] token 기반 인증 시도");
        String accessToken = getToken(request, AuthConstants.ACCESS_TOKEN_HEADER);
        String refreshToken = getToken(request, AuthConstants.REFRESH_TOKEN_HEADER);
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

    private String getToken(HttpServletRequest request, String tokenHeaderName) {
        return request.getHeader(tokenHeaderName)
                .replace(AuthConstants.TOKEN_PREFIX, "");
    }

}
