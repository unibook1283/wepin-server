package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MulticastMessagePushRequest extends MulticastFcmMessage {

    @NonNull
    private final List<String> tokens;

    @Builder
    public MulticastMessagePushRequest(String title, String body, Map<String, String> data, List<String> tokens) {
        super(title, body);
        if (data != null) {
            getData().putAll(data);
        }
        this.tokens = tokens;
    }

    public MulticastMessage toMessage() {
        return MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(getTitle())
                        .setBody(getBody())
                        .build())
                .addAllTokens(tokens)
                .putAllData(getData())
                .build();
    }

}
