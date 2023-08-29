package com.teamwepin.wepin.global.response.dto;

import lombok.Getter;

@Getter
public class SingleResult<T> extends SuccessResult {
    private final T data;

    public SingleResult(T data) {
        this.data = data;
    }
}
