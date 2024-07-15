package com.ssafy.withme.controller.oauth;

import com.ssafy.withme.controller.feign.KakaoTokenClient;
import com.ssafy.withme.dto.oauth.KakaoTokenDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoTokenClient kakaoTokenClient;

    @Value("${kakao.client.id}")
    private String key;

    @GetMapping("/oauth/kakao/callback")
    public void loginCallback(String code, HttpServletResponse response) throws IOException {

        String contentType = "application/x-www-form-urlencoded;charset=utf-8";

        KakaoTokenDto.Request kakaoTokenRequestDto = KakaoTokenDto.Request.builder()
                .client_id(key)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri("http://localhost:8080/oauth/kakao/callback")
                .build();

        KakaoTokenDto.Response kakaoResponse = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDto);
        String accessToken = kakaoResponse.getAccess_token();
        String refreshToken = kakaoResponse.getRefresh_token();
        String accessTokenExpireTime = kakaoResponse.getExpires_in();
        String refreshTokenExpireTime = kakaoResponse.getRefresh_token_expires_in();

        String redirectUri = UriComponentsBuilder
                .fromUriString("http://localhost:3000/oauth/success")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("accessTokenExpireTime", accessTokenExpireTime)
                .queryParam("refreshTokenExpireTime", refreshTokenExpireTime)
                .build().toUriString();

        response.sendRedirect(redirectUri);
    }
}
