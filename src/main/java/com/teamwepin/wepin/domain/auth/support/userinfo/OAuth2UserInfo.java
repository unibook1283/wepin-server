package com.teamwepin.wepin.domain.auth.support.userinfo;

import com.teamwepin.wepin.domain.user.entity.User;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    default User toEntity() {
        return User.builder()
                .email(getEmail())
                .name(getName())
                .provider(getProvider())
                .providerId(getProviderId())
//                .role(Role.USER.toString())
                .build();
    }
}
