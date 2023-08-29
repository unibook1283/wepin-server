package com.teamwepin.wepin.global.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ErrorResult {

    @Schema(description = "요청 결과", defaultValue = "error")
    private final String status = CommonResponse.ERROR.getStatus();
    @Schema(description = "메시지")
    private final String message;
    private final String code;

    public ErrorResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
