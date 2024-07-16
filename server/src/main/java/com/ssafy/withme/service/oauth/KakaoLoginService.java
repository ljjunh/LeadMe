package com.ssafy.withme.service.oauth;

import com.ssafy.withme.controller.feign.KakaoLoginClient;
import com.ssafy.withme.domain.user.constant.GrantType;
import com.ssafy.withme.domain.user.constant.UserType;
import com.ssafy.withme.dto.oauth.KakaoLoginResponseDto;
import com.ssafy.withme.dto.oauth.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginApiService{

    private final KakaoLoginClient client;

    private final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {

        log.info("accessToken: {}", accessToken);

        KakaoLoginResponseDto kakaoUserInfo = client.getKakaoUserInfo(CONTENT_TYPE, "Bearer " + accessToken);
        KakaoLoginResponseDto.KakaoAccount account = kakaoUserInfo.getKakaoAccount();

        log.info("kakaoUserInfo: {}", kakaoUserInfo);
        log.info("account: {}", account);
        log.info("profile: {}", account.getProfile());

        String email = account.getEmail();

        return OAuthAttributes.builder()
                .id(Long.parseLong(kakaoUserInfo.getId()))
                // email은 입력 필수값으로 지정할 수 없었기 때문에, email이 담겨왔는지 먼저 체크
                // email이 없으면 id값을 저장할 수 있도록 설계
                .email(!StringUtils.hasText(email) ? kakaoUserInfo.getId() : email)
                .nickname(account.getProfile().getNickname())
                .birthYear(account.getBirthyear())
                .birthDay(account.getBirthday())
                .birthDayType(account.getBirthday_type())
                .gender(account.getGender())
                .profile(account.getProfile().getThumbnailImageUrl())
                .userType(UserType.KAKAO)
                .build();
    }

    @Override
    public Long logout(String accessToken) {

        log.info("accessToken: {}", accessToken);

        Map<String, Object> response = client.logoutKakaoUser(CONTENT_TYPE, GrantType.BEARER.getType() + " " + accessToken);
        log.info("Logout response: {}", response);

        // 필요한 값 추출 및 반환 (예: 응답에서 상태 코드 또는 특정 값을 추출하여 반환)
        return response != null ? 1L : 0L;
    }
}
