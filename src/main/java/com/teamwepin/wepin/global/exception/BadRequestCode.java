package com.teamwepin.wepin.global.exception;

import com.teamwepin.wepin.domain.jwt.exception.ExpiredJwtException;
import com.teamwepin.wepin.domain.jwt.exception.InvalidTokenException;
import com.teamwepin.wepin.tests.sample.SampleNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BadRequestCode {

    NOT_FOUND_ERROR_CODE("COMMON-001", "해당 에러의 에러코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class),

    SAMPLE_NOT_FOUND("SAMPLE-001", "존재하지 않는 sample입니다.", SampleNotFoundException.class),

    INVALID_TOKEN("AUTH-001", "유효하지 않은 토큰입니다.", InvalidTokenException.class),
    EXPIRED_JWT("AUTH-002", "만료된 토큰입니다.", ExpiredJwtException.class)
    ;


    private final String code;
    private final String message;
    private final Class<? extends BadRequestException> type;

    public static BadRequestCode findByClass(Class<?> type) {
        return Arrays.stream(BadRequestCode.values())
                .filter(code -> code.type.equals(type))
                .findAny()
                .orElseThrow(NotFoundErrorCodeException::new);
    }

}
