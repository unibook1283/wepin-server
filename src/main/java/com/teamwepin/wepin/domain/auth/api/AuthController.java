package com.teamwepin.wepin.domain.auth.api;


import com.teamwepin.wepin.domain.auth.dto.LoginRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 인증", description = "유저 인증 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

//    @Deprecated // filter에서 걸러져서 처리되기 때문에 여기서 처리될 일 없음. swagger를 위해 작성한 api.
    @PostMapping("/login")
    @Operation(summary = "기존 계정 로그인", description = "소셜 없이 하는 로그인")
    public LoginRes existingAccountLogin(
            @Parameter(description = "아이디", required = true) @RequestParam String username,
            @Parameter(description = "비밀번호", required = true) @RequestParam String password
    ) {
        return LoginRes.builder().build();
    }

    @PostMapping("/login/oauth/{providerName}")
    @Operation(summary = "소셜 로그인", description = "카카오/구글 소셜 로그인")
    public LoginRes socialLogin(
            @Parameter(description = "프로바이더명. \"kakao\" 또는 \"google\")", required = true) @PathVariable String providerName,
            @Parameter(description = "resource server로부터 얻은 access token", required = true) @RequestParam String accessToken) {
        return LoginRes.builder().build();
    }

}
