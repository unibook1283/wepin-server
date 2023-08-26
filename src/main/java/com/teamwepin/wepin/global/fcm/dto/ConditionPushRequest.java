package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ConditionPushRequest implements SingleFcmMessage {

    // notification
    private String title;
    private String body;

    @Builder.Default
    private Map<String, String> data = new HashMap<>();

    @NonNull
    private String condition;

    @Override
    public Message toMessage() {
        return Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .setCondition(condition)
                .putAllData(data)
                .build();
    }

}
