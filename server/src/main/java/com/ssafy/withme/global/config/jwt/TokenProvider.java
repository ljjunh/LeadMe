package com.ssafy.withme.global.config.jwt;

import com.ssafy.withme.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 타입은 JWT
                .setIssuer(jwtProperties.getIssuer()) // 내용 : 프로퍼티에스에서 지정한 발급자명
                .setIssuedAt(now) // 내용 issue at : 현재 시간
                .setExpiration(expiry) // 내용 exp : expiry 멤버 변수값
                .setSubject(user.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id", user.getId()) // 클레임 id : 유저 id
                .compact();
    }

    // JWT 토큰 유효성 검증메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀키로 복호화
                    .parseClaimsJws(token); // 클레임이란 받아온 정보(토큰)를 jwt 페이로드에 넣는 것이다.
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    // 토큰 기반으로 스프링 시큐리티 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        // 반환값은 스프링 시큐리티 인증이다.
        // 페이로드에 저장된 클레임 정보를 반환 받는다.
        Claims claims = getClaims(token);

        // ROLE_USER의 권환들
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        // 스프링 시큐리티 사용자 인증을 반환한다.
        // 사용자 이름과 비밀번호를 이용한 인증을 진행한다. JWT 클레임에서 JWT가 발급된 사용자를 가져온다.
        return new UsernamePasswordAuthenticationToken(
                // 시큐리티 유저 정보에 클레임에서 받아온 유저 이메일 정보, 비밀번호, 권한을 넣고 생성, 토큰, 권한목록을 통해 인증 정보 반환
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),
                token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // 토큰을 분석하면서 claims을 빼낸다.
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }


}
