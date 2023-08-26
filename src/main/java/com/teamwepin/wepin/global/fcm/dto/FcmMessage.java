package com.teamwepin.wepin.global.fcm.dto;

import java.util.Map;

public interface FcmMessage {
    String getTitle();
    String getBody();
    Map<String, String> getData();
}
