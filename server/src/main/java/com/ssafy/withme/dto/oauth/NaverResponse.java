package com.ssafy.withme.dto.oauth;

import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.domain.user.constant.RoleType;
import com.ssafy.withme.domain.user.constant.UserStatus;
import com.ssafy.withme.global.util.SHA256Util;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attributes;

    public NaverResponse(Map<String, Object> attributes) {
        System.out.println(attributes);
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    public String makeNickname() {
        String email = getEmail();
        return hashString(email);
    }


    private String hashString(String input) {
        return SHA256Util.hashString(input);
    }

    @Override
    public User toEntity() {

        return User.builder()
                .email(getEmail())
                .name(getName())
                .roleType(RoleType.USER)
                .userStatus(UserStatus.ACTIVE)
                .nickname(makeNickname())
                .build();
    }
}
