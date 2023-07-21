package com.teamwepin.wepin.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final BadRequestCode codeAndMessage = BadRequestCode.findByClass(this.getClass());

    private final String message;
    private final String code;

    public BadRequestException() {
        this.message = codeAndMessage.getMessage();
        this.code = codeAndMessage.getCode();
    }
}
