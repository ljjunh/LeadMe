package com.ssafy.withme.global.exception;

import com.ssafy.withme.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends BusinessException{

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
