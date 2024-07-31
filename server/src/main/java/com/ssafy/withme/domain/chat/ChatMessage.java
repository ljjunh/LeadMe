package com.ssafy.withme.domain.chat;

import com.ssafy.withme.domain.chat.constant.MessageType;
import com.ssafy.withme.dto.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@Document(collection = "chat")
public class ChatMessage {

    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private Long userId; // 채팅 보낸 userId
    private String message; // 메시지
    private String time; // 메시지 보낸 시간

    public static ChatMessage of(ChatMessageDto dto) {
        return ChatMessage.builder()
                .type(dto.getType())
                .roomId(dto.getRoomId())
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .time(LocalDateTime.now().toString())
                .build();
    }
}