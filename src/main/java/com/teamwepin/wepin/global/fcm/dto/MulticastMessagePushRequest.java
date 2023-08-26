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
@Builder
public class MulticastMessagePushRequest implements MulticastFcmMessage {

    // notification
    private String title;
    private String body;

    @Builder.Default
    private Map<String, String> data = new HashMap<>();

    @NonNull
    private List<String> tokens;

    public MulticastMessage toMessage() {
        return MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .addAllTokens(tokens)
                .putAllData(data)
                .build();
    }

}
