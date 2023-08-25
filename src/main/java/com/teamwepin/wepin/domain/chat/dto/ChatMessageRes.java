package com.teamwepin.wepin.domain.chat.dto;

import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
import com.teamwepin.wepin.domain.chat.entity.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatMessageRes {

    private Long chatRoomId;

    private Long senderId;

    private MessageType messageType;

    private String message;

    private LocalDateTime createdAt;

    public static ChatMessageRes of(ChatMessage chatMessage) {
        return ChatMessageRes.builder()
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getId())
                .messageType(chatMessage.getMessageType())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

}
