package com.teamwepin.wepin.domain.jwt.application;

import com.teamwepin.wepin.domain.auth.support.AuthConstants;
import com.teamwepin.wepin.domain.jwt.exception.CustomJwtException;
import com.teamwepin.wepin.domain.jwt.exception.JwtError;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

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
            throw new CustomJwtException(JwtError.JWT_NOT_VALID);
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

    /**
     * HttpServletRequest의 header에서 jwt token을 추출한다.
     * 추출할 수 없을 경우, exception 발생
     */
    public String getTokenFromRequest(HttpServletRequest request, String tokenHeaderName) {
        String headerValue = request.getHeader(tokenHeaderName);
        if (headerValue == null || !headerValue.startsWith(AuthConstants.TOKEN_PREFIX)) {
            throw new CustomJwtException(JwtError.JWT_HEADER_NOT_VALID);
        }
        return headerValue.replace(AuthConstants.TOKEN_PREFIX, "");
    }

}
