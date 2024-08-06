package com.ssafy.withme.service.userchellenge.response;

import com.ssafy.withme.domain.userchallenge.UserChallenge;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserChallengeMyPageResponse {

    //챌린지 아이디
    private Long userChallengeId;

    // 게시글 제목
    private String title;

    // 썸네일 파일
    private byte[] thumbnail;

    @Builder
    private UserChallengeMyPageResponse(Long userChallengeId, String title, byte[] thumbnail) {
        this.userChallengeId = userChallengeId;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public static UserChallengeMyPageResponse responseOf(UserChallenge userChallenge, byte[] thumbnail){
        return UserChallengeMyPageResponse.builder()
                .title(userChallenge.getFileName())
                .thumbnail(thumbnail)
                .userChallengeId(userChallenge.getId())
                .build();
    }
}
