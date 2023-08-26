package com.teamwepin.wepin.global.fcm.dto;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class FcmMessage {

    // notification
    private final String title;
    private final String body;

    private final Map<String, String> data = new HashMap<>();

    public FcmMessage(String title, String body) {
        this.title = title;
        this.body = body;
    }

}
