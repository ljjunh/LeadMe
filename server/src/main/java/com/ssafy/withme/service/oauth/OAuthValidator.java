package com.ssafy.withme.service.oauth;

import com.ssafy.withme.domain.user.constant.UserType;
import com.ssafy.withme.global.error.ErrorCode;
import com.ssafy.withme.global.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class OAuthValidator {

    public void validUserType(String userType){

        if (!UserType.isMemberType(userType))
            throw new BusinessException(ErrorCode.INVALID_USER_TYPE);
    }
}
