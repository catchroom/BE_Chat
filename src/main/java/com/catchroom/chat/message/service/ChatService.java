package com.catchroom.chat.message.service;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.global.pubsub.RedisPublisher;
import com.catchroom.chat.message.repository.ChatRoomRepository;
import com.catchroom.chat.message.type.MessageType;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

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
    public void sendChatMessage(ChatMessageDto chatMessage) {
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
        chatRoomRepository.setLastChatMessage(chatMessage.getRoomId(), chatMessage);

        redisPublisher.publish(chatMessage);
    }

}
