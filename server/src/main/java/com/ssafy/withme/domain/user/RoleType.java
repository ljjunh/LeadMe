package com.ssafy.withme.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType {
    ADMIN("관리자"),
    USER("회원");

    private final String description;
}
