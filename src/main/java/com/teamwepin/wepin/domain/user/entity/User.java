package com.teamwepin.wepin.domain.user.entity;

import com.teamwepin.wepin.domain.chat.entity.UserChatRoom;
import com.teamwepin.wepin.domain.user.dto.UserReq;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Setter
    private String refreshToken;

    private String provider;

    private String providerId;

    private String name;    // provider에서 받아온 name

    @Setter
    private String fcmToken;

    @Setter
    private LocalDateTime fcmTokenModifiedAt;

    @OneToMany(mappedBy = "user")
    private List<UserChatRoom> userChatRooms = new ArrayList<>();

    public void enterChatRoom(UserChatRoom userChatRoom) {
        userChatRooms.add(userChatRoom);
        userChatRoom.setUser(this);
    }

    public void addUserInfo(UserReq userReq) {
        this.email = userReq.getEmail();
        this.password = userReq.getPassword();
    }

}
