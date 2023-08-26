package com.teamwepin.wepin.global.fcm;

import com.teamwepin.wepin.global.fcm.dto.TokenPushRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FcmServiceTest {

    @Autowired
    FcmService fcmService;

    @Test
    void sendSingleMessage() {
        fcmService.sendMessage(TokenPushRequest.builder()
                .title("qwer")
                .body("asdf")
                .token("zxcv")
                .build()
        );
    }

    @Test
    void SendListOfSingleMessage() {
    }

    @Test
    void SendMulticastMessage() {
    }
}