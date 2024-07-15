package com.ssafy.withme.service.oauth;

import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.domain.user.constant.RoleType;
import com.ssafy.withme.domain.user.constant.UserType;
import com.ssafy.withme.dto.oauth.OAuthAttributes;
import com.ssafy.withme.dto.oauth.OAuthDto;
import com.ssafy.withme.global.error.ErrorCode;
import com.ssafy.withme.global.exception.AuthenticationException;
import com.ssafy.withme.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthLoginService {

    private final UserService userService;

    @Transactional
    public OAuthAttributes oauthLogin(OAuthDto.Request request, UserType userType) {

        log.info("userType: {}", userType.getType());

        SocialLoginApiService socialLoginService = SocialLoginApiServiceFactory.getSocialLoginService(userType);

        OAuthAttributes userInfo = socialLoginService.getUserInfo(request.getAccessToken());

        log.info("userInfo : {}", userInfo);
        log.info("accessToken : {}", request.getAccessToken());
        log.info("expireTime : {}", request.getAccessTokenExpireTime());

        Optional<User> findUser = userService.findById(userInfo.getId());

        if (findUser.isEmpty()){

            User joinUser = userInfo.toUserEntity(userType, RoleType.USER);

            userService.joinByEntity(joinUser);
        }

        findUser.ifPresent(u -> u.updateRefreshToken(request.getRefreshToken()));

        return userInfo;
    }
}
