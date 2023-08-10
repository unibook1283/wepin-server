package com.teamwepin.wepin.domain.auth.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 인증", description = "유저 인증 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

//    @Deprecated // filter에서 걸러져서 처리되기 때문에 여기서 처리될 일 없음. swagger를 위해 작성한 api.
    @PostMapping("/users/login")
    @Operation(summary = "기존 계정 로그인", description = "소셜 없이 하는 로그인")
    public void login(
            @Parameter(description = "아이디", required = true) @RequestParam String username,
            @Parameter(description = "비밀번호", required = true) @RequestParam String password
    ) {

    }

}
