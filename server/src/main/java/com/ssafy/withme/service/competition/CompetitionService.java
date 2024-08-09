package com.ssafy.withme.service.competition;

import com.ssafy.withme.controller.competition.request.CompetitionCreateRequest;
import com.ssafy.withme.controller.competition.request.PasswordVerificationRequest;
import com.ssafy.withme.domain.competition.Competition;
import com.ssafy.withme.domain.competition.constant.CompetitionStatus;
import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.global.annotation.CurrentUser;
import com.ssafy.withme.global.exception.EntityNotFoundException;
import com.ssafy.withme.global.util.SHA256Util;
import com.ssafy.withme.repository.competition.CompetitionRepository;
import com.ssafy.withme.repository.user.UserRepository;
import com.ssafy.withme.service.competition.response.CompetitionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ssafy.withme.global.error.ErrorCode.NOT_FOUND_COMPETITION;
import static com.ssafy.withme.global.error.ErrorCode.USER_NOT_EXISTS;

@RequiredArgsConstructor
@Service
@Slf4j
public class CompetitionService {
    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String SESSION_KEY_PREFIX = "session:";

    /**
     * 세션 생성
     *
     * @param request
     * @param sessionId
     */
    public void create(CompetitionCreateRequest request, String sessionId, @CurrentUser User user) {

        log.info("세션 생성 : 유저 아아디" + user.getId() + " 유저 이메일 : " + user.getEmail());

        // 해당 세션으로 레디스 데이터 생성
        setSessionCount(sessionId, 0L);

        Competition competition = Competition.builder()
                                .user(user)
                                .roomName(request.getRoomName())
                                .sessionId(sessionId)
                                .isPublic(request.isPublic())
                                .password(SHA256Util.hashString(request.getPassword()))
                                .build();

        competitionRepository.save(competition);



    }

    /**
     * OPEN 상태와 방 이름 기반으로 페이징 처리하여 조회
     * @param pageNo
     * @param criteria
     * @param size
     * @return
     */
    public List<CompetitionResponse> getCompetitions(int pageNo, String criteria, int size, String searchKeyword) {

        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, criteria));
        Page<CompetitionResponse> page;
        if(searchKeyword == null || searchKeyword.isEmpty()) {
            page = competitionRepository.findByStatus(CompetitionStatus.OPEN, pageable).map(CompetitionResponse :: toResponse);
        } else {
            page = competitionRepository.findByStatusAndRoomNameContains(CompetitionStatus.OPEN, searchKeyword, pageable).map(CompetitionResponse :: toResponse);
        }
        return page.getContent();
    }

    /**
     * OPEN 상태이고 방이름 기반으로 경쟁전의 개수를 조회
     * @return
     */
    public int getTotlaCompetitions(String searchKeyword) {
        return competitionRepository.countByStatusAndRoomNameContains(CompetitionStatus.OPEN, searchKeyword);
    }

    /**
     * 경쟁전 방 비밀번호와 입력된 비밀번호 비교
     * @param request
     * @return
     */
    public boolean verifyCompetitionsPassword(PasswordVerificationRequest request) {

        Competition competition = competitionRepository.findById(request.getCompetitionId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_COMPETITION));

        return competition.getPassword().equals(SHA256Util.hashString(request.getPassword()));
    }

    /**
     * 세션 아이디를 활용해 유저 아이디 반환
     * @param sessionId
     * @return
     */
    public Long getCreateUserId(String sessionId) {

        Competition competition = competitionRepository.findBySessionId(sessionId);
        return competition.getCreateUser().getId();
    }

    public Long incrementSessionCount(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId + ":count";
        return redisTemplate.opsForValue().increment(key);
    }

    public Long decrementSessionCount(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId + ":count";
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long getSessionCount(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId + ":count";
        String count = redisTemplate.opsForValue().get(key);
        return count != null ? Long.valueOf(count) : 0L;
    }

    public void setSessionCount(String sessionId, Long count) {
        String key = SESSION_KEY_PREFIX + sessionId + ":count";
        redisTemplate.opsForValue().set(key, String.valueOf(count));
    }

    public void deleteSessionCount(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId + ":count";
        redisTemplate.delete(key);
    }
}
