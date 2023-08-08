package com.teamwepin.wepin.domain.auth.jwt;

import com.teamwepin.wepin.domain.jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void createAccessToken() {
        String accessToken = jwtProvider.createAccessToken("1");

        System.out.println("accessToken = " + accessToken);
    }

    @Test
    void getPayload() {
        String accessToken = jwtProvider.createAccessToken("1");

        String payload = jwtProvider.getPayload(accessToken);

        assertThat(payload).isEqualTo("1");
    }

    @Test
    void validateToken() {
        String accessToken = jwtProvider.createAccessToken("1");

        boolean res = jwtProvider.validateToken(accessToken);

        assertThat(res).isTrue();
    }
}