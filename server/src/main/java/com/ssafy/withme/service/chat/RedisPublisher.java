package com.ssafy.withme.service.chat;

import com.ssafy.withme.dto.chat.MessageSubDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    private ChannelTopic getChannelTopic(Long roomId) {
        return new ChannelTopic("/sub/chat/message/" + roomId); // 방별로 고유한 토픽 생성
    }

    public void publish(MessageSubDto message, Long roomId) {

        ChannelTopic channelTopic = getChannelTopic(roomId); // 방의 토픽 가져오기

        log.info("RedisPublisher publishing to room {}: {}", roomId, message.getChatMessageDto().getMessage());

        log.info("topic : {} ", channelTopic.getTopic());
        redisTemplate.convertAndSend(channelTopic.getTopic(), message.getChatMessageDto().getMessage()); // 해당 방으로 메시지 발행
    }
}
