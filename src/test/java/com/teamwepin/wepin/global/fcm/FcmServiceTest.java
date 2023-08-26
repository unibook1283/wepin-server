package com.teamwepin.wepin.global.fcm;

import com.teamwepin.wepin.global.fcm.dto.MulticastMessagePushRequest;
import com.teamwepin.wepin.global.fcm.dto.TokenPushRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class FcmServiceTest {

    @Autowired
    FcmService fcmService;

    @Test
    void sendSingleMessage() {
        Map<String, String> data = new HashMap<>();
        data.put("qwer", "asdf");
        fcmService.sendMessage(TokenPushRequest.builder()
                        .title("qwer")
                        .body("asdf")
                        .token("qwer")
//                        .data(data)
                .build()
        );
        fcmService.sendMessage(MulticastMessagePushRequest.builder()
                .title("qwer")
                .body("body")
                .tokens(Arrays.asList("qwer", "asdf"))
                .data(data)
                .build());
    }

    @Test
    void SendListOfSingleMessage() {
    }

    @Test
    void SendMulticastMessage() {
    }
}