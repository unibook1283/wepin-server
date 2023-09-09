package com.teamwepin.wepin.domain.chat.dto;

import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomCreateRes {

    @Schema(description = "채팅방 id")
    private Long chatRoomId;

    public static ChatRoomCreateRes of(ChatRoom chatRoom) {
        return ChatRoomCreateRes.builder()
                .chatRoomId(chatRoom.getId())
                .build();
    }

}
