package com.ssafy.withme.global.config.oauth;

import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.global.config.jwt.TokenProvider;
import com.ssafy.withme.repository.user.RefreshTokenRepository;
import com.ssafy.withme.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 스프링 시큐리티에서 별도의 authenticationSuccessHandler를 지정하지 않으면
    // 로그인 성공 이후 SimpleUrlAuthenticationSuccessHandler 사용한다.

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(1);

    // 로그인 성공 시 리다이렉트 페이지
    public static final String REDIRECT_PATH = "";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    // 성공적으로 로그인 하는 경우에 토큰과 관련된 작업을 추가로 처리하기 위해 오버라이드함
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // 인증된 principal(주체)를 반환한다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttribute("email"));
    }
}
