package com.teamwepin.wepin.domain.user.api;

import com.teamwepin.wepin.domain.user.application.UserService;
import com.teamwepin.wepin.domain.user.dto.FcmTokenReq;
import com.teamwepin.wepin.domain.user.dto.JoinRes;
import com.teamwepin.wepin.domain.user.dto.UserReq;
import com.teamwepin.wepin.global.response.ResponseService;
import com.teamwepin.wepin.global.response.dto.SingleResult;
import com.teamwepin.wepin.global.response.dto.SuccessResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저", description = "유저 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ResponseService responseService;

    @PostMapping("/users/join")
    @Operation(summary = "회원가입", description = "회원가입 api. return value 고칠 것.")
    public SingleResult<JoinRes> join(
            @Parameter(description = "회원가입에 필요한 정보", required = true) @RequestBody UserReq userReq) {
        // password encoding
        String encodedPassword = passwordEncoder.encode(userReq.getPassword());
        userReq.setPassword(encodedPassword);   // set말고 다른 방법 고민
        return responseService.getSingleResult(userService.join(userReq));
    }

    @PostMapping("/users/{userId}/fcmToken")
    @Operation(summary = "유저의 fcm registration token 저장", description = "일단은 매 로그인 성공 후 호출하는 것으로 생각중. https://firebase.google.com/docs/cloud-messaging/manage-tokens?hl=ko&authuser=0 참고")
    public SuccessResult saveFcmToken(
            @Parameter(required = true) @PathVariable Long userId,
            @Parameter(required = true) @RequestBody FcmTokenReq fcmTokenReq) {
        userService.saveFcmToken(userId, fcmTokenReq);
        return responseService.getSuccessResult();
    }

}
