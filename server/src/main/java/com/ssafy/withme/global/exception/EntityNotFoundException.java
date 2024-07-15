package com.ssafy.withme.global.exception;

import com.ssafy.withme.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
