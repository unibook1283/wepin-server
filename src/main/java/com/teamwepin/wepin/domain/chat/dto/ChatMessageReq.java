package com.teamwepin.wepin.domain.chat.dto;

import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReq {

    private String message;

    public ChatMessage toEntity(ChatRoom chatRoom) {
        return ChatMessage.builder()
                .message(message)
                .chatRoom(chatRoom)
                .build();
    }

}
