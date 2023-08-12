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

    @Schema(description = "아이디")
    private String username;

    @Setter
    @Schema(description = "비밀번호")
    private String password;

    // cf) 이메일은 구글 로그인 성공 시 받아옴.
    @Schema(description = "이메일")
    private String email;

    public User toEntity() {
        return User.builder()
                .id(userId)
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

}
