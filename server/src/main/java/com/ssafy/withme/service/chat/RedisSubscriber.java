package com.ssafy.withme.service.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.withme.dto.chat.ChatMessageDto;
import com.ssafy.withme.dto.chat.response.ChatRoomGetResponse;
import com.ssafy.withme.dto.chat.MessageSubDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면
     * 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {

        log.info("publishMessage: {}", publishMessage);

        try {
            MessageSubDto messageSubDto = objectMapper.readValue(publishMessage, MessageSubDto.class);

            log.info("Redis Subscriber chatMSG : {}", messageSubDto.getChatMessageDto().getMessage());

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend(
                    "/sub/chat/message/" + messageSubDto.getChatMessageDto().getRoomId(), messageSubDto.getChatMessageDto()
            );

        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

}
