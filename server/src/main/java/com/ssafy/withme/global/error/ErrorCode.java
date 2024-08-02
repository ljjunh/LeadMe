package com.ssafy.withme.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 인증 && 인가
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 refresh token은 만료되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 토큰은 access token이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "관리자 Role이 아닙니다."),
    NOT_EQUAL_PASSWORD(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "Password가 일치하지 않습니다."),

    // 회원,
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "잘못된 회원 타입입니다."),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "이미 가입된 회원입니다."),
    USER_NOT_EXISTS(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 회원은 존재하지 않습니다."),
    NOT_EXISTS_EMAIL(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "일치하는 Email 정보가 존재하지 않습니다."),
    NOT_EXISTS_PASSWORD(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "Password가 일치하지 않습니다."),
    USERNAME_IS_NULL(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Username이 null 입니다."),
    FAILED_UNFOLLOW(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "Unfollow할 컬럼이 존재하지 않거나, Unfollow를 실패했습니다."),
    NOT_AUTHORIZATION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 권한이 없는 사용자입니다."),

    // 챌린지
    NOT_EXISTS_CHALLENGE(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 챌린지 영상은 존재하지 않습니다."),
    NOT_EXISTS_USER_CHALLENGE_FILE(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 챌린지 영상은 존재하지 않습니다."),
    
    // 경쟁전
    NOT_EXISTS_COMPETITON_SESSION(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "존재하지 않는 방입니다.")
    
    ;

    ErrorCode(HttpStatus httpStatus, Integer statusCode, String message) {
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private Integer statusCode;
    private String message;
}
