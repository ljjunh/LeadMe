package com.ssafy.withme.domain.dto;

import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.domain.user.constant.RoleType;
import com.ssafy.withme.domain.user.constant.UserStatus;
import lombok.RequiredArgsConstructor;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attributes;

    public GoogleResponse(Map<String, Object> attributes) {
        System.out.println(attributes);
        this.attributes = attributes;
    }


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public User toEntity() {

        return User.builder()
                .name(getName())
                .email(getEmail())
                .profileImg(attributes.get("picture").toString())
                .roleType(RoleType.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
