package com.teamwepin.wepin.domain.chat.api;

import com.teamwepin.wepin.domain.chat.application.ChatService;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomCreateRes;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomJoinReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomRes;
import com.teamwepin.wepin.global.response.ResponseService;
import com.teamwepin.wepin.global.response.dto.ListResult;
import com.teamwepin.wepin.global.response.dto.SingleResult;
import com.teamwepin.wepin.global.response.dto.SuccessResult;
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
    private final ResponseService responseService;

    @PostMapping("/chatRoom")
    @Operation(summary = "채팅방 생성", description = "채팅방 생성")
    public SingleResult<ChatRoomCreateRes> createChatRoom(
            @Parameter(required = true) @RequestBody ChatRoomReq chatRoomReq) {
        return responseService.getSingleResult(chatService.createChatRoom(chatRoomReq));
    }

    @GetMapping("/users/{userId}/chatRoom")
    @Operation(summary = "유저가 속한 모든 채팅방 조회")
    public ListResult<ChatRoomRes> getChatRoomsOfUser(
            @Parameter(required = true) @PathVariable Long userId) {
        return responseService.getListResult(chatService.getChatRoomsOfUser(userId));
    }

    @PostMapping("/chatRoom/{chatRoomId}/join")
    @Operation(summary = "채팅방 참여", description = "채팅방에 유저들이 추가로 참여")
    public SuccessResult joinChatRoom(
            @Parameter(name = "채팅방 번호", required = true) @PathVariable Long chatRoomId,
            @Parameter(required = true) @RequestBody ChatRoomJoinReq chatRoomJoinReq) {
        chatService.joinChatRoom(chatRoomId, chatRoomJoinReq.getUserIds());
        return responseService.getSuccessResult();
    }

}
