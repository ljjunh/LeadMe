package com.ssafy.withme.infra.oauth.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOauthConfig(
        String redirectUrl,
        String clientId,
        String clientSecret,
        String[] scope
) {

}
