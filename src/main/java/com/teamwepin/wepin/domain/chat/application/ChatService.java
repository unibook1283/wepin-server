package com.teamwepin.wepin.domain.chat.application;

import com.teamwepin.wepin.domain.chat.dao.ChatMessageRepository;
import com.teamwepin.wepin.domain.chat.dao.ChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dao.UserChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dto.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    @Transactional
    public ChatRoomCreateRes createChatRoom(ChatRoomReq chatRoomReq) {
        ChatRoom chatRoom = ChatRoom.builder()
                .userChatRooms(new ArrayList<>())
                .chatMessages(new ArrayList<>())
                .build();
        chatRoomRepository.save(chatRoom);

        List<Long> userIds = chatRoomReq.getUserIds();
        joinChatRoom(chatRoom.getId(), userIds);

        return ChatRoomCreateRes.of(chatRoom);
    }

    @Transactional
    public void joinChatRoom(Long chatRoomId, List<Long> userIds) {
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
    }

    @Transactional(readOnly = true)
    public List<ChatRoomRes> getChatRoomsOfUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByUser(user);

        return userChatRooms.stream()
                .map(userChatRoom -> {
                    ChatRoom chatRoom = userChatRoom.getChatRoom();
                    List<UserChatRoom> otherUserChatRooms = userChatRoom.getChatRoom().getUserChatRooms().stream()
                            .filter(userChatRoom1 -> !userChatRoom1.getUser().getId().equals(userId))
                            .collect(Collectors.toList());
                    User otherUser = otherUserChatRooms.get(0).getUser();
                    String otherUserNickname = otherUser.getNickname();

                    ChatMessage lastChatMessage = chatMessageRepository.findFirstByChatRoomOrderByCreatedAtDesc(chatRoom);
                    String lastMessageText = lastChatMessage != null ? lastChatMessage.getMessage() : null;
                    LocalDateTime lastChatTime = lastChatMessage != null ? lastChatMessage.getCreatedAt() : null;

                    return ChatRoomRes.builder()
                            .chatRoomId(chatRoom.getId())
                            .chatRoomName(otherUserNickname)
                            .lastChatMessage(lastMessageText)
                            .lastChatTime(lastChatTime)
                            .chatRoomImage("image")
                            .unreadCount(userChatRoom.getUnreadCount())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageRes sendChatMessage(Long chatRoomId, ChatMessageReq chatMessageReq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
        User sender = userRepository.findById(chatMessageReq.getSenderId())
                .orElseThrow(UserNotFoundException::new);

        ChatMessage chatMessage = chatMessageReq.toEntity(chatRoom, sender);

        chatMessageRepository.save(chatMessage);

        chatRoom.getUserChatRooms().stream()
                .filter(userChatRoom -> !userChatRoom.getUser().getId().equals(sender.getId()))
                .forEach(UserChatRoom::increaseUnreadCount);

        try {
            sendChatMessagePushNotification(chatRoom, sender, chatMessage);
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 예외 발생. 정상 흐름 변환.");
            e.printStackTrace();
        }
        return ChatMessageRes.of(chatMessage);
    }

    private void sendChatMessagePushNotification(ChatRoom chatRoom, User sender, ChatMessage chatMessage) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByChatRoom(chatRoom);
        List<String> userTokens = userChatRooms.stream()
                .filter(userChatRoom -> !userChatRoom.getUser().getId().equals(sender.getId()))   // sender는 제외
                .map(userChatRoom -> userChatRoom.getUser().getFcmToken())
                .filter(Objects::nonNull)   // user의 fcmToken이 null이면 pass
                .collect(Collectors.toList());
        Map<String, String> data = new HashMap<String, String>() {{
            put("chatRoomId", chatRoom.getId().toString());
            put("chatMessageCreatedAt", chatMessage.getCreatedAt().toString());
        }};
        fcmService.sendMessage(MulticastMessagePushRequest.builder()
                .title(sender.getNickname())
                .body(chatMessage.getMessage())
                .tokens(userTokens)
                .data(data) // 채팅방 정보
                .build());
    }

}
