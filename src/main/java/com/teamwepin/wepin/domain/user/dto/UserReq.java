package com.teamwepin.wepin.domain.user.dto;

import com.teamwepin.wepin.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReq {

    @Schema(description = "소셜로그인 후 회원가입하는 경우 넣을 것", nullable = true)
    private Long userId;

    @Schema(description = "이메일")
    private String email;

    @Setter
    @Schema(description = "비밀번호")
    private String password;


    public User toEntity() {
        return User.builder()
                .id(userId)
                .password(password)
                .email(email)
                .build();
    }

}
