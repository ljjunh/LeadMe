package com.ssafy.withme.dto.user;

import com.ssafy.withme.domain.user.User;

public record SearchUserDto(Long id, String nickname, String profile) {

    public static SearchUserDto fromUser(User user) {

        return new SearchUserDto(user.getId(), user.getNickname(), user.getProfileImg());
    }
}
