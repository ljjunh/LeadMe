package com.ssafy.withme.service.comment;

import com.ssafy.withme.controller.comment.request.CommentUpdateRequest;
import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.domain.user.constant.RoleType;
import com.ssafy.withme.global.error.ErrorCode;
import com.ssafy.withme.global.exception.AuthorizationException;
import com.ssafy.withme.repository.user.UserRepository;
import com.ssafy.withme.service.comment.response.CommentDeleteResponse;
import com.ssafy.withme.controller.comment.request.CommentCreateRequest;
import com.ssafy.withme.controller.comment.request.CommentDeleteRequest;
import com.ssafy.withme.domain.comment.Comment;
import com.ssafy.withme.domain.userchallenge.UserChallenge;
import com.ssafy.withme.repository.comment.CommentRepository;
import com.ssafy.withme.repository.userchallenge.UserChallengeRepository;
import com.ssafy.withme.service.comment.response.CommentCreateResponse;
import com.ssafy.withme.service.comment.response.CommentUpdateResponse;
import com.ssafy.withme.service.comment.response.CommentViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;


    public List<CommentViewResponse> findCommentByChallengeId(Pageable pageable, Long userChallengeId) {
        List<Comment> findCommentByUserChallengeId = commentRepository.findByUserChallengeId(userChallengeId, pageable);
        List<CommentViewResponse> comments = findCommentByUserChallengeId.stream()
                .map(comment -> CommentViewResponse.of(comment))
                .collect(Collectors.toList());
        return comments;
    }


    /**
     * user도 파라미터로 추가해야함.
     * @param request
     */
    public CommentCreateResponse create(CommentCreateRequest request) {

        UserChallenge userChallenge = userChallengeRepository.findById(request.getUserChallengeId()).get();
        Comment newComment = Comment.builder()
                .content(request.getContent())
//                .user(user)
                .userChallenge(userChallenge)
                .build();
        Comment saveComment = commentRepository.save(newComment);
        return CommentCreateResponse.of(saveComment);
    }

    public CommentDeleteResponse delete(CommentDeleteRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId()).get();
        Long userId = request.getUserId();
        User findUser = userRepository.findById(userId).get();
        // 댓글 작성하 유저가 아니고, 관리자도 아니라면?
        if(findUser != comment.getUser() || findUser.getRoleType() != RoleType.ADMIN){
            throw new AuthorizationException(ErrorCode.NOT_AUTHORIZATION);
        }
        commentRepository.delete(comment);
        return CommentDeleteResponse.of(comment);
    }

    public CommentUpdateResponse update(CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(request.getCommentId()).get();
        Long userId = request.getUserId();
        User findUser = userRepository.findById(userId).get();
        // 댓글 작성하 유저가 아니고, 관리자도 아니라면?
        if(findUser != comment.getUser() || findUser.getRoleType() != RoleType.ADMIN){
            throw new AuthorizationException(ErrorCode.NOT_AUTHORIZATION);
        }
        comment.changeContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentUpdateResponse.of(updatedComment);
    }
}
