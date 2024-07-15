package com.ssafy.withme.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus {

    INACTIVE("휴면계정"),
    ACTIVE("활동계정");

    private final String description;


}
