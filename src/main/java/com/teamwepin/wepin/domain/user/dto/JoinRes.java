package com.teamwepin.wepin.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinRes {

    private Long userId;
    private String accessToken;
    private String refreshToken;

}
