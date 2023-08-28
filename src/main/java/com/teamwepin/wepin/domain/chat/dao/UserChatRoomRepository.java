package com.teamwepin.wepin.domain.chat.dao;

import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import com.teamwepin.wepin.domain.chat.entity.UserChatRoom;
import com.teamwepin.wepin.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);

    List<UserChatRoom> findByUser(User user);

    List<UserChatRoom> findByChatRoom(ChatRoom chatRoom);
}
