package com.ssafy.withme.service.oauth;

import com.ssafy.withme.domain.user.constant.UserType;
import com.ssafy.withme.global.error.ErrorCode;
import com.ssafy.withme.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SocialLoginApiServiceFactory {

    private static Map<String, SocialLoginApiService> map;

    public SocialLoginApiServiceFactory(Map<String, SocialLoginApiService> map) {

        this.map = map;
    }

    public static SocialLoginApiService getSocialLoginService(UserType userType) {

        String beanName = "";

        if (UserType.KAKAO.equals(userType))
            beanName = "kakaoLoginService";
        else throw new BusinessException(ErrorCode.INVALID_USER_TYPE);

        // Bean Name으로 구현체 추출
        return map.get(beanName);
    }
}
