package com.teamwepin.wepin.domain.user.application;

import com.teamwepin.wepin.domain.jwt.application.JwtService;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.dto.FcmTokenReq;
import com.teamwepin.wepin.domain.user.dto.JoinRes;
import com.teamwepin.wepin.domain.user.dto.UserReq;
import com.teamwepin.wepin.domain.user.entity.User;
import com.teamwepin.wepin.domain.user.exception.UserAlreadyJoinedException;
import com.teamwepin.wepin.domain.user.exception.UserNotFoundException;
import com.teamwepin.wepin.domain.user.exception.EmailExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public JoinRes join(UserReq userReq) {
        if (userRepository.findByEmail(userReq.getEmail()).isPresent()) {
            throw new EmailExistException();
        }
        User user = null;
        if (userReq.getUserId() != null) {
            user = userRepository.findById(userReq.getUserId())
                    .orElseThrow(UserNotFoundException::new);
            if (user.getEmail() != null) {
                throw new UserAlreadyJoinedException();
            }
            user.addUserInfo(userReq);
        } else {
            user = userRepository.save(userReq.toEntity());
        }

        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken(user.getEmail());
        user.setRefreshToken(refreshToken);

        return JoinRes.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void saveFcmToken(Long userId, FcmTokenReq fcmTokenReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.setFcmToken(fcmTokenReq.getFcmToken());
        user.setFcmTokenModifiedAt(LocalDateTime.now());
    }

}
