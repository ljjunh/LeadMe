package com.ssafy.withme.dto.oauth;

import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.domain.user.constant.RoleType;
import com.ssafy.withme.domain.user.constant.UserStatus;
import com.ssafy.withme.domain.user.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder @AllArgsConstructor
public class OAuthAttributes {

    private Long id;

    private String nickname;

    private String email;

    private String profile;

    private String gender;

    private String birthYear;

    private String birthDay;

    private String birthDayType;

    private UserType userType;

    public User toUserEntity(UserType userType, RoleType roleType) {

        return User.builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .gender(gender)
                .birthYear(birthYear)
                .birthDay(birthDay)
                .birthDayType(birthDayType)
                .userType(userType)
                .roleType(roleType)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
