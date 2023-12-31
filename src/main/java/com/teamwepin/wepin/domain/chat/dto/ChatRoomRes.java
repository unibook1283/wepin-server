package com.teamwepin.wepin.domain.chat.dto;


import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatRoomRes {

    @Schema(description = "채팅방 id")
    private Long chatRoomId;

    private String chatRoomName;

    private String lastChatMessage;

    private LocalDateTime lastChatTime;

    private String chatRoomImage;

    private Integer unreadCount;

}
