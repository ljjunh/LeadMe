package com.ssafy.withme.service.user;


import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.domain.user.constant.UserStatus;
import com.ssafy.withme.global.config.jwt.TokenProvider;
import com.ssafy.withme.global.error.ErrorCode;
import com.ssafy.withme.global.exception.EntityNotFoundException;
import com.ssafy.withme.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

//    public Long save(AddUserRequest dto) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        return userRepository.save(User.builder()
//                .email(dto.getEmail())
//                .password(encoder.encode(dto.getPassword()))
//                .build()).getId();
//    }
    public User findById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_EXISTS));
    }

    public User findByEmail(String email) {

        log.info("find by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findUserIdByToken(String token) {

        Long userId = tokenProvider.getUserId(token);

        return findById(userId);
    }

    @Transactional
    public void updateStatus(Long id, UserStatus status) {

        User findUser = findById(id);

        findUser.updateStatus(status);
    }
}
