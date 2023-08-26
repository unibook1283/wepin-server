package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
public class TokenPushRequest extends SingleFcmMessage {

    @NonNull
    private final String token;

    @Builder
    public TokenPushRequest(String title, String body, Map<String, String> data, String token) {
        super(title, body);
        if (data != null) {
            getData().putAll(data);
        }
        this.token = token;
    }

    @Override
    public Message toMessage() {
        return Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(getTitle())
                        .setBody(getBody())
                        .build())
                .setToken(token)
                .putAllData(getData())
                .build();
    }

}
