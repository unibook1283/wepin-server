package com.teamwepin.wepin.domain.chat.entity;


import com.teamwepin.wepin.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChatRoom {

    @Id
    @GeneratedValue
    @Column(name = "user_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    @Setter
    private ChatRoom chatRoom;

    public static UserChatRoom createUserChatRoom(User user, ChatRoom chatRoom) {
        return UserChatRoom.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }

}
