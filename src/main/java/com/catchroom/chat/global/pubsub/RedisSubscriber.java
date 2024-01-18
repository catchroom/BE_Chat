package com.catchroom.chat.global.pubsub;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Redis에서 메시지가 발행(publish)되면
     * 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
            log.info("Redis Subcriber publishMsg: {}", publishMessage);

            // ChatMessage 객채로 맵핑
            ChatMessageDto chatMessage = objectMapper.readValue(publishMessage, ChatMessageDto.class);

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    public void sendRoomList(String message) {
        try {
            log.info("Redis Subcriber  room publishMsg: {}", message);
            // ChatMessage 객채로 맵핑
            ChatMessageDto chatMessage = objectMapper.readValue(message, ChatMessageDto.class);
//            chatMessage.setMessage("메세지를 보냈습니다!");
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/999cf498-bba9-43d9-9b00-d363aca2e288", chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}
