package com.teamwepin.wepin.domain.chat.dto;

import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatMessageRes {

    private Long id;

    private String message;

    public static ChatMessageRes of(ChatMessage chatMessage) {
        return ChatMessageRes.builder()
                .id(chatMessage.getId())
                .message(chatMessage.getMessage())
                .build();
    }

}
