package com.teamwepin.wepin.domain.auth.jwt;

import com.teamwepin.wepin.domain.jwt.application.JwtService;
import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void createAccessToken() {
        String accessToken = jwtService.createAccessToken("1");

        System.out.println("accessToken = " + accessToken);
    }

    @Test
    void getPayload() {
        String accessToken = jwtService.createAccessToken("1");

        String payload = jwtService.getPayload(accessToken);

        assertThat(payload).isEqualTo("1");
    }

    @Test
    void validateToken() {
        String accessToken = jwtService.createAccessToken("1");

        boolean res = jwtService.validateToken(accessToken);

        assertThat(res).isTrue();
    }

    @Test
    void invalidTokenException() {
        assertThrows(CustomJwtException.class, () -> jwtService.getPayload("asdf"));
    }

}