package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ConditionPushRequest extends SingleFcmMessage {

    @NonNull
    private final String condition;

    @Builder
    public ConditionPushRequest(String title, String body, Map<String, String> data, String condition) {
        super(title, body);
        if (data != null) {
            getData().putAll(data);
        }
        this.condition = condition;
    }

    @Override
    public Message toMessage() {
        return Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(getTitle())
                        .setBody(getBody())
                        .build())
                .setCondition(condition)
                .putAllData(getData())
                .build();
    }

}
