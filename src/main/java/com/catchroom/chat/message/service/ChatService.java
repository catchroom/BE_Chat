package com.catchroom.chat.message.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.global.common.SuccessMessage;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.global.pubsub.RedisPublisher;
import com.catchroom.chat.message.dto.MessageSubDto;
import com.catchroom.chat.message.repository.ChatRoomRepository;
import com.catchroom.chat.message.type.MessageType;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final MainFeignClient mainFeignClient;

    /**
     * destination 정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessageDto chatMessage, String accessToken) {
        chatRoomRepository.setLastChatMessage(chatMessage.getRoomId(), chatMessage);

        if (chatMessage.getType().equals(MessageType.DELETE)) {
            deleteChatRoom(accessToken, chatMessage.getRoomId());
        }

        List<ChatRoomListGetResponse> chatRoomListGetResponseList =
            chatRoomService.getChatRoomList(accessToken);

        MessageSubDto messageSubDto = MessageSubDto.builder()
            .chatMessageDto(chatMessage)
            .list(chatRoomListGetResponseList)
            .build();

        redisPublisher.publish(messageSubDto);
    }


    /**
     * 채팅방 삭제 로직
     * @param accessToken
     * @param roomId
     */
    private void deleteChatRoom(String accessToken, String roomId) {
        log.info("=>> 채팅방 삭제 {} start ", roomId);
        SuccessMessage message = mainFeignClient.deleteChatRoom(accessToken, roomId);
        log.info("=>> 채팅방 삭제 {} Msg : {}", roomId, message.Meassage());
    }

}
