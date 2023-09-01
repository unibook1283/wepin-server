package com.teamwepin.wepin.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamwepin.wepin.domain.auth.dto.LoginRes;
import com.teamwepin.wepin.domain.auth.support.AuthConstants;
import com.teamwepin.wepin.domain.jwt.application.JwtService;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.global.response.ResponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String filterProcessesUrl = "/api/v1/login/email";

    private final JwtService jwtService;
    private final ResponseService responseService;
    private final UserRepository userRepository;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, ResponseService responseService, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.responseService = responseService;
        this.userRepository = userRepository;
        setFilterProcessesUrl(filterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[AUTH] username, password 기반 인증 시도");
        String username = request.getParameter(AuthConstants.USERNAME_PARAMETER);
        username = (username != null) ? username.trim() : "";
        String password = request.getParameter(AuthConstants.PASSWORD_PARAMETER);
        password = (password != null) ? password : "";

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("[AUTH] username, password 기반 인증 성공");
        request.getParameter(AuthConstants.USERNAME_PARAMETER);
        String username = (String) authResult.getPrincipal();
        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken(username);

        Long userId = jwtService.setRefreshTokenToUser(username, refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        LoginRes loginRes = LoginRes.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        String jsonResponse = objectMapper.writeValueAsString(responseService.getSingleResult(loginRes));

        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("[AUTH] username, password 기반 인증 실패");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", "AUTH");
        body.put("message", failed.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

}
