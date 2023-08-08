package com.teamwepin.wepin.domain.user.api;

import com.teamwepin.wepin.domain.user.application.UserService;
import com.teamwepin.wepin.domain.user.dto.UserReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "유저 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/users/join")
    @Operation(summary = "회원가입", description = "회원가입 api. return value 고칠 것.")
    public Long join(
            @Parameter(description = "회원가입에 필요한 정보", required = true) @RequestBody UserReq userReq) {
        // password encoding
        String encodedPassword = passwordEncoder.encode(userReq.getPassword());
        userReq.setPassword(encodedPassword);   // set말고 다른 방법 고민
        return userService.join(userReq);
    }

}
