package com.teamwepin.wepin.domain.chat.dao;

import com.teamwepin.wepin.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
