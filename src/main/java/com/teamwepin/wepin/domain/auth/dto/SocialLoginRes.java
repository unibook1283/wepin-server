package com.teamwepin.wepin.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "isNew가 true이면 토큰이 주어지지 않음. 회원가입을 거쳐 토큰 발급받을 것. isNew가 false이면 토큰이 주어짐.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SocialLoginRes {

    private Long userId;

    @Schema(description = "true -> 회원가입이 필요한 유저")
    private Boolean isNew;

    @Schema(nullable = true)
    private String accessToken;

    @Schema(nullable = true)
    private String refreshToken;

}
