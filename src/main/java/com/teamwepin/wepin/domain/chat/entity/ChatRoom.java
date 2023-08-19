package com.teamwepin.wepin.domain.chat.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;

    @OneToMany(mappedBy = "chatRoom")
    private List<UserChatRoom> userChatRooms = new ArrayList<>();

    public void addUserChatRoom(UserChatRoom userChatRoom) {
        userChatRooms.add(userChatRoom);
        userChatRoom.setChatRoom(this);
    }

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages = new ArrayList<>();

}
