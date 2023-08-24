package com.teamwepin.wepin.domain.chat.api;

import com.teamwepin.wepin.domain.chat.application.ChatService;
import com.teamwepin.wepin.domain.chat.dto.ChatMessageReq;
import com.teamwepin.wepin.domain.chat.dto.ChatMessageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;

    @MessageMapping("/chatRoom/{chatRoomId}")
    @SendTo("/sub/chatRoom/{chatRoomId}")
    public ChatMessageRes sendChatMessage(
            @DestinationVariable Long chatRoomId,
            @RequestBody ChatMessageReq chatMessageReq) {
        return chatService.sendChatMessage(chatRoomId, chatMessageReq);
    }

}
