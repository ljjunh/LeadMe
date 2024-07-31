package com.ssafy.withme.controller.challenge;

import com.ssafy.withme.controller.challenge.request.ChallengeCreateRequest;
import com.ssafy.withme.domain.BaseEntity;
import com.ssafy.withme.domain.landmark.Landmark;
import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.global.annotation.CurrentUser;
import com.ssafy.withme.global.response.SuccessResponse;
import com.ssafy.withme.service.challege.ChallengeService;
import com.ssafy.withme.service.challege.response.ChallengeCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChallengeController extends BaseEntity {

    private final ChallengeService challengeService;

    /**
     * 클라이언트가 youtubeURL로 요청하면 영상을 저장하고, 몽고디비에 스켈레톤 데이터를 저장한다.
     * @param request
     */
    @PostMapping("/api/v1/challenge")
    public SuccessResponse<ChallengeCreateResponse> createChallenge(@CurrentUser User user, @RequestBody ChallengeCreateRequest request){
        return SuccessResponse.of(challengeService.createChallenge(request));
    }


    /**
     * 프론트엔드에서 youtubeId를 파라미터로 담아 요청하면 스켈레톤 데이터를 반환한다.
     * @param youtubeId
     * @return
     */
    @GetMapping("/api/v1/challenge/{youtubeId}")
    public SuccessResponse<Landmark> findLandMarkByVideoName(@PathVariable String youtubeId) throws Exception {
        return SuccessResponse.of(challengeService.getLandMarkByYoutubeId(youtubeId));
    }







}
