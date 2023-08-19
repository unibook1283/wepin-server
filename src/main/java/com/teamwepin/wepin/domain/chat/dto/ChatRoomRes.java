package com.teamwepin.wepin.domain.chat.dto;


import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomRes {

    @Schema(description = "채팅방 id")
    private Long id;

    public static ChatRoomRes of(ChatRoom chatRoom) {
        return ChatRoomRes.builder()
                .id(chatRoom.getId())
                .build();
    }

}
