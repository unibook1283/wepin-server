package com.teamwepin.wepin.domain.jwt.exception;

import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException {

	private final String code;
	private final String message;

	public CustomJwtException(JwtError jwtError) {
		this.code = jwtError.getCode();
		this.message = jwtError.getMessage();
	}

}
