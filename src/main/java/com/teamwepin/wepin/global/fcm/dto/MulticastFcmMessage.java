package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.MulticastMessage;

import java.util.Map;

public abstract class MulticastFcmMessage extends FcmMessage {
    public MulticastFcmMessage(String title, String body) {
        super(title, body);
    }

    public abstract MulticastMessage toMessage();
}
