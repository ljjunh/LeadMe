package com.ssafy.withme.service.chat.chatroom;

import com.ssafy.withme.domain.chat.ChatMessage;
import com.ssafy.withme.domain.chat.feign.MainFeignClient;
import com.ssafy.withme.dto.ChatMessageDto;
import com.ssafy.withme.dto.ChatRoomGetResponse;
import com.ssafy.withme.global.response.SuccessMessage;
import com.ssafy.withme.repository.chat.ChatRoomRedisRepository;
import com.ssafy.withme.service.chat.message.ChatMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

    private final MainFeignClient mainFeignClient;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatMongoService chatMongoService;

    public ChatRoomGetResponse getChatRoomInfo(String accessToken, Long userId, String roomId) {
//        return mainFeignClient.getChatRoomInfo(accessToken, roomId);
        return chatRoomRedisRepository.getChatRoom(userId, roomId);
    }

    public List<ChatRoomGetResponse> getChatRoomListByFeign(Long userId, String accessToken) {
        // 처음 HTTP 요청에서는 무조건 레디스 초기화 진행하도록 로직 수정

        // Feign Client를 사용하는 것이 아닌 직접 redis에서 채팅내역 조회
        List<ChatRoomGetResponse> chatRoomListGetResponseList = chatRoomRedisRepository.getChatRoomList(userId);
//        List<ChatRoomGetResponse> chatRoomListGetResponseList = mainFeignClient.getChatRoomList(accessToken);

        log.info("chatRoomListGetResponseList: {}", chatRoomListGetResponseList);
        chatRoomListGetResponseList.forEach(this::setListChatLastMessage);
        chatRoomRedisRepository.initChatRoomList(userId, chatRoomListGetResponseList);
        return sortChatRoomListLatest(chatRoomListGetResponseList);
    }

    public List<ChatRoomGetResponse> getChatRoomList(Long userId, String accessToken) {

        List<ChatRoomGetResponse> chatRoomListGetResponseList = null;
        if (chatRoomRedisRepository.existChatRoomList(userId)) {
            chatRoomListGetResponseList = chatRoomRedisRepository.getChatRoomList(userId);

        } else {
            // 채팅방이 레디스에 없으면 페인 사용해서 불러온다!
            // -> 레디스에 없으면 그냥 만들자
//            chatRoomListGetResponseList = mainFeignClient.getChatRoomList(accessToken);
            chatRoomRedisRepository.initChatRoomList(userId, chatRoomListGetResponseList);
        }

        chatRoomListGetResponseList.forEach(this::setListChatLastMessage);

        return chatRoomListGetResponseList;
    }

    /**
     * 몽고 디비에서 마지막 메시지 가져와서 저장하는 로직
     * @param chatRoomListGetResponse
     */
    public void setListChatLastMessage(ChatRoomGetResponse chatRoomListGetResponse) {

        // 몽고 디비에서 마지막 메시지 가져와서 저장.
        String chatRoomNumber = chatRoomListGetResponse.getChatRoomNumber();
        if (chatRoomRedisRepository.getLastMessage(chatRoomNumber) != null) {
            chatRoomListGetResponse.updateChatMessageDto(
                    chatRoomRedisRepository.getLastMessage(chatRoomNumber)
            );

        } else {
            ChatMessage chatMessage = chatMongoService.findLatestMessageByRoomId(chatRoomNumber);
            if (chatMessage != null) {
                chatRoomListGetResponse.updateChatMessageDto(
                        ChatMessageDto.fromEntity(chatMessage)
                );
            }
        }
    }

    /**
     * 채팅방 마지막 메시지의 시간들을 비교하여 정렬하는 메소드
     * @param chatRoomListGetResponseList
     */
    public List<ChatRoomGetResponse> sortChatRoomListLatest (
            List<ChatRoomGetResponse> chatRoomListGetResponseList
    ) {
        List<ChatRoomGetResponse> newChatRoomList = new ArrayList<>();
        for (ChatRoomGetResponse response : chatRoomListGetResponseList) {
            if (response.getLastChatMessageDto() != null) newChatRoomList.add(response);
        }

        Collections.sort(newChatRoomList, (o1, o2) ->
                o2.getLastChatMessageDto().getTime().compareTo(o1.getLastChatMessageDto().getTime()));

        return newChatRoomList;
    }

    /**
     * 채팅방 삭제 로직
     * @param accessToken
     * @param roomId
     */
    public void deleteChatRoom(String accessToken, String roomId, Long userId) {
        log.info("=>> 채팅방 삭제 {} start ", roomId);
        SuccessMessage message = mainFeignClient.deleteChatRoom(accessToken, roomId);

        // feign으로 삭제할 게 아니라 service로직에서 직접 삭제해주기
        chatRoomRedisRepository.deleteChatRoom(userId, roomId);
        log.info("=>> 채팅방 삭제 {} Msg : {}", roomId, message.Message());
    }

}
