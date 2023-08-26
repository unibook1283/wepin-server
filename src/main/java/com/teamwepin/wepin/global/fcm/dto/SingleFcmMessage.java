package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.Message;

import java.util.Map;

public abstract class SingleFcmMessage extends FcmMessage {

    public SingleFcmMessage(String title, String body) {
        super(title, body);
    }
    public abstract Message toMessage();
}
