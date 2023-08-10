package com.teamwepin.wepin.domain.jwt.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtError {

	JWT_ACCESS_NOT_VALID("JWT-001", "엑세스 토큰이 유효하지 않습니다."),
	JWT_REFRESH_NOT_VALID("JWT-002", "리프레쉬 토큰이 유효하지 않습니다."),
	JWT_ACCESS_EXPIRED("JWT-003", "엑세스 토큰이 만료되었습니다."),
	JWT_REFRESH_EXPIRED("JWT-004", "리프레쉬 토큰이 만료되었습니다. 다시 로그인 해주세요."),
	JWT_NOT_VALID("JWT-005", "토큰이 유효하지 않습니다."),
	JWT_HEADER_NOT_VALID("JWT-006", "jwt 헤더가 없거나 유효하지 않습니다.")
	;

	private final String code;
	private final String message;
}
