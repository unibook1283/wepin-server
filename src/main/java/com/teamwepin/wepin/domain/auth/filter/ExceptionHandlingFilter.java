package com.teamwepin.wepin.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import com.teamwepin.wepin.global.response.dto.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomJwtException e) {
            setErrorResponse(response, e.getCode(), e.getMessage(), HttpStatus.UNAUTHORIZED);
            printError(e);
        } catch (AuthenticationException | JwtException e) {
            setErrorResponse(response, "AUTH", e.getMessage(), HttpStatus.UNAUTHORIZED);
            printError(e);
        } catch (Exception e) {
            setErrorResponse(response, "EX", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            printError(e);
        }
    }

    private void printError(Exception e) {
        log.error("예외 발생 : {}", e.getMessage(), e);
    }

    private void setErrorResponse(HttpServletResponse response, String code, String message, HttpStatus status) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(new ErrorResult(code, message)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
