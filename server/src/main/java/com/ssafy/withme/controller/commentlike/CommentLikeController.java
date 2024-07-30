package com.ssafy.withme.controller.commentlike;

import com.ssafy.withme.controller.commentlike.request.CommentLikeCreateRequest;
import com.ssafy.withme.global.response.SuccessResponse;
import com.ssafy.withme.service.commentlike.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 좋아요 버튼을 눌렀을 때 호출한다.
    @PostMapping("/api/v1/commentLike")
    public Integer createCommentLike(@RequestBody CommentLikeCreateRequest request) {
        return commentLikeService.create(request);
    }

    @DeleteMapping("/api/v1/commentLike")
    public SuccessResponse<Void> deleteCommentLike(@RequestBody CommentLikeCreateRequest request) {
        commentLikeService.delete(request);
        return SuccessResponse.empty();
    }

}
