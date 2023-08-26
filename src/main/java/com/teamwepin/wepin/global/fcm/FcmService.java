package com.teamwepin.wepin.global.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.teamwepin.wepin.global.fcm.dto.MulticastFcmMessage;
import com.teamwepin.wepin.global.fcm.dto.SingleFcmMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FcmService {

    @FunctionalInterface
    private interface FcmOperation {
        void execute() throws FirebaseMessagingException;
    }

    private static void executeFcmOperation(FcmOperation fcmOperation) {
        try {
            fcmOperation.execute();
            log.info("Successfully sent message");
        } catch (FirebaseMessagingException e) {
            log.error("failed to send firebase message. ErrorCode: " + e.getErrorCode() + ", Message: " + e.getMessage());
        }
    }

    public void sendMessage(SingleFcmMessage request) {
        executeFcmOperation(() -> FirebaseMessaging.getInstance().send(request.toMessage()));
    }

    /**
     * 서로 다른 메시지를 한 번에 전송
     */
    public void sendMessage(List<SingleFcmMessage> request) {
        List<Message> messages = request.stream()
                .map(SingleFcmMessage::toMessage)
                .collect(Collectors.toList());
        executeFcmOperation(() -> FirebaseMessaging.getInstance().sendAll(messages));
    }

    /**
     * 하나의 메시지를 여러개의 토큰에 한 번에 전송
     */
    public void sendMessage(MulticastFcmMessage request) {
        executeFcmOperation(() -> FirebaseMessaging.getInstance().sendMulticast(request.toMessage()));
    }

    /**
     * 현재 프로젝트에서는 비동기 메시지 전송을 사용할 만큼 대량의 메시지를 한번에 보낼 일이 없다고 판단함.
     * 나중에 필요하면 추가할 것.
     */
//    public void sendMessageAsync(SingleFcmMessage request) {
//        ApiFuture<String> stringApiFuture = FirebaseMessaging.getInstance().sendAsync(request.toMessage());
//    }
//
//    public void sendMessageAsync(MulticastFcmMessage request) {
//        ApiFuture<BatchResponse> stringApiFuture = FirebaseMessaging.getInstance().sendMulticastAsync(request.toMessage());
//    }

}
