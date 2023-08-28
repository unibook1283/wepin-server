package com.teamwepin.wepin.domain.chat.application;

import com.teamwepin.wepin.domain.chat.dao.ChatMessageRepository;
import com.teamwepin.wepin.domain.chat.dao.ChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dao.UserChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dto.ChatMessageReq;
import com.teamwepin.wepin.domain.chat.dto.ChatMessageRes;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomRes;
import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import com.teamwepin.wepin.domain.chat.entity.UserChatRoom;
import com.teamwepin.wepin.domain.chat.exception.ChatRoomNotFoundException;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.entity.User;
import com.teamwepin.wepin.domain.user.exception.UserNotFoundException;
import com.teamwepin.wepin.global.fcm.FcmService;
import com.teamwepin.wepin.global.fcm.dto.MulticastMessagePushRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    @Transactional
    public ChatRoomRes createChatRoom(ChatRoomReq chatRoomReq) {
        ChatRoom chatRoom = ChatRoom.builder()
                .userChatRooms(new ArrayList<>())
                .chatMessages(new ArrayList<>())
                .build();
        chatRoomRepository.save(chatRoom);

        List<Long> userIds = chatRoomReq.getUserIds();
        joinChatRoom(chatRoom.getId(), userIds);

        return ChatRoomRes.of(chatRoom);
    }

    @Transactional
    public ChatRoomRes joinChatRoom(Long chatRoomId, List<Long> userIds) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        userIds.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(UserNotFoundException::new))
                .filter(user -> !userChatRoomRepository.findByUserAndChatRoom(user, chatRoom).isPresent())
                .forEach(user -> {
                    UserChatRoom userChatRoom = UserChatRoom.createUserChatRoom(user, chatRoom);
                    userChatRoomRepository.save(userChatRoom);
                    user.enterChatRoom(userChatRoom);
                    chatRoom.addUserChatRoom(userChatRoom);
                });

        return ChatRoomRes.of(chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoomRes getChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
        return ChatRoomRes.of(chatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomRes> getChatRoomsOfUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByUser(user);

        return userChatRooms.stream()
                .map(userChatRoom ->
                        ChatRoomRes.of(userChatRoom.getChatRoom()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageRes sendChatMessage(Long chatRoomId, ChatMessageReq chatMessageReq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
        User user = userRepository.findById(chatMessageReq.getSenderId())
                .orElseThrow(UserNotFoundException::new);

        ChatMessage chatMessage = chatMessageRepository.save(chatMessageReq.toEntity(chatRoom, user));

        // 푸시 알림
        sendChatMessagePushNotification(chatRoom, user, chatMessage);

        return ChatMessageRes.of(chatMessage);
    }

    private void sendChatMessagePushNotification(ChatRoom chatRoom, User user, ChatMessage chatMessage) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoom(chatRoom);
        List<String> userTokens = userChatRooms.stream()
                .map(userChatRoom -> userChatRoom.getUser().getFcmToken())
                .collect(Collectors.toList());
        Map<String, String> data = new HashMap<String, String>() {{
            put("chatRoomId", chatRoom.getId().toString());
            put("chatMessageCreatedAt", chatMessage.getCreatedAt().toString());
        }};
        fcmService.sendMessage(MulticastMessagePushRequest.builder()
                .title(user.getUsername())
                .body(chatMessage.getMessage())
                .tokens(userTokens)
                .data(data) // 채팅방 정보
                .build());
    }

}
