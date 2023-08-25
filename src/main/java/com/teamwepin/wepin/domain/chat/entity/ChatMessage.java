package com.teamwepin.wepin.domain.chat.entity;


import com.teamwepin.wepin.domain.user.entity.User;
import com.teamwepin.wepin.global.support.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private MessageType messageType;

    private String message;

}
