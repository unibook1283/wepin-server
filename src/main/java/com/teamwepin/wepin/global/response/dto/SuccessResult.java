package com.teamwepin.wepin.global.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SuccessResult {

    @Schema(description = "요청 결과", defaultValue = "success")
    private final String status = CommonResponse.SUCCESS.getStatus();
    @Schema(description = "메시지", defaultValue = "요청이 성공적으로 처리되었습니다.")
    private final String message = CommonResponse.SUCCESS.getMessage();

}
