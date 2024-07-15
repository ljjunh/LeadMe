package com.ssafy.withme.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OAuthDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String memberType;

        private String accessToken;

        private String refreshToken;

        private String accessTokenExpireTime;

        private String refreshTokenExpireTime;
    }
}
