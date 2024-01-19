package com.catchroom.chat.message.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.global.pubsub.RedisPublisher;
import com.catchroom.chat.message.dto.MessageSubDto;
import com.catchroom.chat.message.repository.ChatRoomRepository;
import com.catchroom.chat.message.type.MessageType;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

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
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
        chatRoomRepository.setLastChatMessage(chatMessage.getRoomId(), chatMessage);

        List<ChatRoomListGetResponse> chatRoomListGetResponseList =
            chatRoomService.getChatRoomList(accessToken);

        MessageSubDto messageSubDto = MessageSubDto.builder()
            .chatMessageDto(chatMessage)
            .list(chatRoomListGetResponseList)
            .build();

        redisPublisher.publish(messageSubDto);
    }

}
