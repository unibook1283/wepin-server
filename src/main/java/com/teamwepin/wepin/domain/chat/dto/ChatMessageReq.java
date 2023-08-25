package com.teamwepin.wepin.domain.chat.dto;

import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import com.teamwepin.wepin.domain.chat.entity.MessageType;
import com.teamwepin.wepin.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReq {

    private Long senderId;

    private MessageType messageType;

    private String message;

    public ChatMessage toEntity(ChatRoom chatRoom, User user) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .user(user)
                .messageType(messageType)
                .message(message)
                .build();
    }

}
