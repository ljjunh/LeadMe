package com.ssafy.withme.service.user;


import com.ssafy.withme.controller.feign.KakaoLoginClient;
import com.ssafy.withme.domain.user.User;
import com.ssafy.withme.global.error.ErrorCode;
import com.ssafy.withme.global.exception.EntityNotFoundException;
import com.ssafy.withme.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final KakaoLoginClient client;

    public Optional<User> findById(Long id) {

        return userRepository.findById(id);
    }

    public Long joinByEntity(User user) {

        return userRepository.save(user).getId();
    }

    @Transactional
    public void updateToken(Long id, String refreshToken) {

        User findUser = findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_EXISTS));

        findUser.getRefreshToken().updateRefreshToken(refreshToken);
    }
}
