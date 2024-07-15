package com.ssafy.withme.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class KakaoTokenDto {

    @Builder @Getter @AllArgsConstructor
    public static class Request {

        private String grant_type;
        private String client_id;
        private String redirect_uri;
        private String code;
        private String client_secret;
    }

    @Builder @Getter @AllArgsConstructor
    public static class Response {

        private String token_type;
        private String access_token;
//        private String id_token;
        private String expires_in;
        private String refresh_token;
        private String refresh_token_expires_in;
        private String scope;
    }
}
