package com.teamwepin.wepin.domain.chat.application;

import com.teamwepin.wepin.domain.chat.dao.ChatMessageRepository;
import com.teamwepin.wepin.domain.chat.dao.ChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dao.UserChatRoomRepository;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomReq;
import com.teamwepin.wepin.domain.chat.dto.ChatRoomRes;
import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
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

    public ChatRoomRes getChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);
        return ChatRoomRes.of(chatRoom);
    }

}
