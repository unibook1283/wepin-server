package com.teamwepin.wepin.domain.auth.support.userinfo;

import com.teamwepin.wepin.domain.user.entity.User;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    default User toEntity() {
        return User.builder()
//                .email(getEmail())    // 이메일은 회원가입에서 직접 받을 것.
                .name(getName())
                .provider(getProvider())
                .providerId(getProviderId())
//                .role(Role.USER.toString())
                .build();
    }
}
