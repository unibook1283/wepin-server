package com.teamwepin.wepin.domain.auth.service;

import com.teamwepin.wepin.domain.auth.dto.SocialLoginRes;
import com.teamwepin.wepin.domain.auth.exception.InvalidProviderNameException;
import com.teamwepin.wepin.domain.auth.support.userinfo.GoogleUserInfo;
import com.teamwepin.wepin.domain.auth.support.userinfo.KakaoUserInfo;
import com.teamwepin.wepin.domain.auth.support.userinfo.OAuth2UserInfo;
import com.teamwepin.wepin.domain.jwt.application.JwtService;
import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import com.teamwepin.wepin.domain.jwt.exception.JwtError;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final InMemoryClientRegistrationRepository inMemoryRepository;  // application-oauth properties 정보를 갖고 있음.
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public SocialLoginRes login(String providerName, String resourceServerAccessToken) {
        checkProviderName(providerName);
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        User user = getUserProfile(providerName, resourceServerAccessToken, provider);
        String username = user.getUsername();

        if (username == null) {
            return SocialLoginRes.builder()
                    .userId(user.getId())
                    .isNew(true)
                    .build();
        }

        String accessToken = jwtService.createAccessToken(username);
        String refreshToken = jwtService.createRefreshToken(username);
        user.setRefreshToken(refreshToken);

        return SocialLoginRes.builder()
                .userId(user.getId())
                .isNew(false)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void checkProviderName(String providerName) {
        if (providerName.equals("kakao") || providerName.equals("google")) {
            return;
        }
        log.error("Invalid provider name. providerName = {}", providerName);
        throw new InvalidProviderNameException();
    }

    private User getUserProfile(String providerName, String resourceServerAccessToken, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, resourceServerAccessToken);
        OAuth2UserInfo oAuth2UserInfo = null;
        if (providerName.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(userAttributes);
        } else if (providerName.equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(userAttributes);
        }

        OAuth2UserInfo finalOAuth2UserInfo = oAuth2UserInfo;    // 람다 캡처링
        return userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId())
                .orElseGet(() -> userRepository.save(finalOAuth2UserInfo.toEntity()));
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, String resourceServerAccessToken) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(resourceServerAccessToken))
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(error ->
                                Mono.error(new CustomJwtException(JwtError.JWT_TOKEN_NOT_VALID))))  // 토큰 말고 다른 문제일 수도 있는데
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

}
