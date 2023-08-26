package com.teamwepin.wepin.global.fcm.dto;

import com.google.firebase.messaging.MulticastMessage;

public interface MulticastFcmMessage extends FcmMessage{
    MulticastMessage toMessage();
}
