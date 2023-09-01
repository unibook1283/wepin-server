package com.teamwepin.wepin.domain.auth.authProvider;

import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
        if (passwordEncoder.matches(password, user.getPassword())) {
//            return UsernamePasswordAuthenticationToken.authenticated(username, password, );   // isAuthenticated...
            return new UsernamePasswordAuthenticationToken(username, password); // token에 authority를 설정할 수 있음.
        } else {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
