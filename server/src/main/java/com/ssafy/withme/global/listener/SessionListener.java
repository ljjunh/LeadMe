package com.ssafy.withme.global.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
@RequiredArgsConstructor
public class SessionListener implements HttpSessionListener {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVATE_KEY = "active_user";

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

        String sessionId = httpSessionEvent.getSession().getId();

        redisTemplate.opsForSet().add(ACTIVATE_KEY, sessionId);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        String sessionId = httpSessionEvent.getSession().getId();

        redisTemplate.opsForSet().remove(ACTIVATE_KEY, sessionId);
    }

    public Long getActiveUserCount() {

        return redisTemplate.opsForSet().size(ACTIVATE_KEY);
    }
}