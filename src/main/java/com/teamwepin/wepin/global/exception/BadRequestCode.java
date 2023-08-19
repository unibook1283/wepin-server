package com.teamwepin.wepin.global.exception;

import com.teamwepin.wepin.domain.auth.exception.InvalidProviderNameException;
import com.teamwepin.wepin.domain.chat.exception.ChatRoomNotFoundException;
import com.teamwepin.wepin.domain.user.exception.UserAlreadyJoinedException;
import com.teamwepin.wepin.domain.user.exception.UserNotFoundException;
import com.teamwepin.wepin.domain.user.exception.UsernameExistException;
import com.teamwepin.wepin.tests.sample.SampleNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum BadRequestCode {

    NOT_FOUND_ERROR_CODE("COMMON-001", "해당 에러의 에러코드를 찾을 수 없습니다.", NotFoundErrorCodeException.class),

    SAMPLE_NOT_FOUND("SAMPLE-001", "존재하지 않는 sample입니다.", SampleNotFoundException.class),

    USER_NOT_FOUND("USER-001", "존재하지 않는 user입니다.", UserNotFoundException.class),
    INVALID_PROVIDER_NAME("USER-003", "유효하지 않은 providerName입니다.", InvalidProviderNameException.class),
    USERNAME_ALREADY_EXIST("USER-004", "이미 존재하는 username입니다.", UsernameExistException.class),
    USER_ALREADY_JOINED("USER-005", "이미 가입한 유저입니다.", UserAlreadyJoinedException.class),

    CHAT_ROOM_NOT_FOUND("CHAT-001", "존재하지 않는 chatRoom입니다.", ChatRoomNotFoundException.class),
    ;


    private final String code;
    private final String message;
    private final Class<? extends BadRequestException> type;

    public static BadRequestCode findByClass(Class<?> type) {
        return Arrays.stream(BadRequestCode.values())
                .filter(code -> code.type.equals(type))
                .findAny()
                .orElseThrow(NotFoundErrorCodeException::new);
    }

}
