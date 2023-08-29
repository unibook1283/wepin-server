package com.teamwepin.wepin.global.response.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResult<T> extends SuccessResult {

    private final List<T> data;

    public ListResult(List<T> data) {
        this.data = data;
    }

}
