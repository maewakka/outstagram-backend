package com.woo.outstagram.util.jwt;

import com.woo.outstagram.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.access-token-expire-time}")
    private Long expireTime;
    @Value("${jwt.secret}")
    private String secretKey;
    private final CustomUserDetailsService customUserDetailsService;


    /**
     * Claim의 사용자 Email을 포함시켜 JWT 토큰을 생성해준다.
     * @param email
     */
    public String createJwt(String email) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 토큰에서 사용자 정보를 빼 Authentication 인증 객체를 반환한다.
     * @param token
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        String email = claims.get("email").toString();

        UserDetails principal = customUserDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    /**
     * 토큰이 유효한지 검증한다. 1이면 유효, -1이면 유효하지 않는다.
     * @param token
     */
    public int validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }
}
