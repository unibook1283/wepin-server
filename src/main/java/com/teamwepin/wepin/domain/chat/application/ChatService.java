package com.teamwepin.wepin.domain.chat.application;

import com.teamwepin.wepin.domain.chat.dao.ChatMessageRepository;
import com.teamwepin.wepin.domain.chat.dao.ChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dao.UserChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dto.ChatMessageReq;
import com.teamwepin.wepin.domain.chat.dto.ChatMessageRes;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomRes;
import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import com.teamwepin.wepin.domain.chat.entity.UserChatRoom;
import com.teamwepin.wepin.domain.chat.exception.ChatRoomNotFoundException;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.entity.User;
import com.teamwepin.wepin.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

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

        return ChatMessageRes.of(chatMessageRepository.save(chatMessageReq.toEntity(chatRoom, user)));
    }

}
