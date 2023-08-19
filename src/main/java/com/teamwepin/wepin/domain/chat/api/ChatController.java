package com.teamwepin.wepin.domain.chat.api;

import com.teamwepin.wepin.domain.chat.application.ChatService;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomJoinReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채팅")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chatRoom")
    @Operation(summary = "채팅방 생성", description = "채팅방 생성")
    public ChatRoomRes createChatRoom(
            @Parameter(required = true) @RequestBody ChatRoomReq chatRoomReq) {
        return chatService.createChatRoom(chatRoomReq);
    }

    @GetMapping("/chatRoom/{chatRoomId}")
    @Operation(summary = "채팅방 조회", description = "채팅방 조회")
    public ChatRoomRes getChatRoom(
            @Parameter(required = true) @PathVariable Long chatRoomId) {
        return chatService.getChatRoom(chatRoomId);
    }

    @PostMapping("/chatRoom/{chatRoomId}/join")
    @Operation(summary = "채팅방 참여", description = "채팅방에 유저들이 추가로 참여")
    public ChatRoomRes joinChatRoom(
            @Parameter(name = "채팅방 번호", required = true) @PathVariable Long chatRoomId,
            @Parameter(required = true) @RequestBody ChatRoomJoinReq chatRoomJoinReq) {
        return chatService.joinChatRoom(chatRoomId, chatRoomJoinReq.getUserIds());
    }

}
