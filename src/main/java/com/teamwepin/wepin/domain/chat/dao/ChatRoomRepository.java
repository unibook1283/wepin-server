package com.teamwepin.wepin.domain.chat.dao;

import com.teamwepin.wepin.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
