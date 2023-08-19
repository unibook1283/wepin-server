package com.teamwepin.wepin.domain.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomJoinReq {

    @Schema(description = "채팅방에 참여할 유저들의 id 리스트")
    private List<Long> userIds;

}
