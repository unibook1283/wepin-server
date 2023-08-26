package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;

@Getter
public class TopicPushRequest extends SingleFcmMessage {

    @NonNull
    private final String topic;

    @Builder
    public TopicPushRequest(String title, String body, Map<String, String> data, String topic) {
        super(title, body);
        if (data != null) {
            getData().putAll(data);
        }
        this.topic = topic;
    }

    @Override
    public Message toMessage() {
        return Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(getTitle())
                        .setBody(getBody())
                        .build())
                .setTopic(topic)
                .putAllData(getData())
                .build();
    }

}
