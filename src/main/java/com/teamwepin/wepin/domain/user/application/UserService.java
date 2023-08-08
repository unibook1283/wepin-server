package com.teamwepin.wepin.domain.user.application;

import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.dto.UserReq;
import com.teamwepin.wepin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long join(UserReq userReq) {
        User user = userRepository.save(userReq.toEntity());
        return user.getId();
    }

}
