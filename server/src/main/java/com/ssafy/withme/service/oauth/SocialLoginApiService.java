package com.ssafy.withme.service.oauth;

import com.ssafy.withme.dto.oauth.OAuthAttributes;

public interface SocialLoginApiService {

    OAuthAttributes getUserInfo(String accessToken);

    Long logout(String accessToken);
}
