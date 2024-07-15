package com.ssafy.withme.domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum UserType {

    KAKAO("kakao"),
    NAVER("naver")
    ;

    private final String type;

    public static UserType from(String type){

        return UserType.valueOf(type.toUpperCase());
    }

    public static boolean isMemberType(String type){

        List<UserType> list = Arrays.stream(UserType.values())
                .filter(memberType -> memberType.name().equals(type))
                .toList();

        return !list.isEmpty();
    }
}
