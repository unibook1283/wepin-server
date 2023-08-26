package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.Message;

public interface SingleFcmMessage extends FcmMessage {
    Message toMessage();
}
