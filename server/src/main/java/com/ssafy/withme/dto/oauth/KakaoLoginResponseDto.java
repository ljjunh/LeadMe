package com.ssafy.withme.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class KakaoLoginResponseDto {

    private String id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter @Builder
    public static class KakaoAccount{

        private String email;

        private String birthyear; // YYYY

        private String birthday; // MMDD

        private String birthday_type; // SOLAR-양력, LUNAR-음력

        private String gender;

        private Profile profile;

        @Getter @Builder
        public static class Profile{

            private String nickname;

            @JsonProperty("thumbnail_image_url")
            private String thumbnailImageUrl;
        }
    }
}
