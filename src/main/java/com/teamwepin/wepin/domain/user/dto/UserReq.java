package com.teamwepin.wepin.domain.user.dto;

import com.teamwepin.wepin.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReq {

    @Schema(description = "아이디")
    private String username;

    @Setter
    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "이메일")
    private String email;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

}
