package com.teamwepin.wepin.domain.jwt.application;

import com.teamwepin.wepin.domain.auth.support.AuthConstants;
import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import com.teamwepin.wepin.domain.jwt.exception.JwtError;
import com.teamwepin.wepin.domain.user.dao.UserRepository;
import com.teamwepin.wepin.domain.user.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.teamwepin.wepin.domain.auth.support.AuthConstants.ACCESS_TOKEN_HEADER;
import static com.teamwepin.wepin.domain.auth.support.AuthConstants.REFRESH_TOKEN_HEADER;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    private final UserRepository userRepository;

    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(String payload) {
        return createToken(payload, refreshTokenValidityInMilliseconds);
    }

    private String createToken(String payload, long expireLength) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);
        Claims claims = Jwts.claims()
                .setSubject(payload);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getPayload(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomJwtException(JwtError.JWT_TOKEN_NOT_VALID);
        }
    }

    public boolean validateToken(String token) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody().getExpiration();

            return expiration.after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getAccessTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader(ACCESS_TOKEN_HEADER);
        if (headerValue == null || !headerValue.startsWith(AuthConstants.ACCESS_TOKEN_PREFIX)) {
            throw new CustomJwtException(JwtError.JWT_ACCESS_NOT_VALID);
        }
        return headerValue.replace(AuthConstants.ACCESS_TOKEN_PREFIX, "");
    }

    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader(REFRESH_TOKEN_HEADER);
        if (headerValue == null) {
            throw new CustomJwtException(JwtError.JWT_REFRESH_NOT_VALID);
        }
        return headerValue;
    }

    @Transactional
    public Long setRefreshTokenToUser(String username, String refreshToken) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username에 해당하는 유저를 찾을 수 없습니다."));
        user.setRefreshToken(refreshToken);
        return user.getId();
    }

}
