package com.teamwepin.wepin.global.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponse {

    SUCCESS("success", "요청이 성공적으로 처리되었습니다."),
    ERROR("error", "요청에 실패했습니다.");

    private final String status;
    private final String message;

}
