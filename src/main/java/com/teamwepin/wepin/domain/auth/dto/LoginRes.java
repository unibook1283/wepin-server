package com.teamwepin.wepin.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRes {

    private String accessToken;
    private String refreshToken;

}
