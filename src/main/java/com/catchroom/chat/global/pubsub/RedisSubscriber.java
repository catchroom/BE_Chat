package com.catchroom.chat.global.pubsub;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.dto.MessageSubDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

            ChatMessageDto chatMessage = objectMapper.readValue(publishMessage, MessageSubDto.class).getChatMessageDto();

            log.info("Redis Subcriber MSG publishMsg : {}", chatMessage.getMessage());

            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    public void sendRoomList(String publishMessage) {
        try {
            log.info("Redis Subcriber room publishMsg ing..");

            ChatMessageDto chatMessage = objectMapper.readValue(publishMessage, MessageSubDto.class).getChatMessageDto();
            List<ChatRoomListGetResponse> chatRoomListGetResponseList = objectMapper.readValue(publishMessage, MessageSubDto.class)
                .getList();
            sortChatRoomListLatest(chatRoomListGetResponseList);
            // 로그인 유저 채팅방 리스트 최신화
            messagingTemplate.convertAndSend("/sub/chat/roomlist/" + chatMessage.getUserId(), chatRoomListGetResponseList);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    private void sortChatRoomListLatest(List<ChatRoomListGetResponse> chatRoomListGetResponseList) {
        Comparator<ChatRoomListGetResponse> comparator = new Comparator<ChatRoomListGetResponse>() {
            @Override
            public int compare(ChatRoomListGetResponse o1, ChatRoomListGetResponse o2) {
                if (o1.getLastChatmessageDto() != null && o2.getLastChatmessageDto() != null) {
                    return LocalDateTime.parse(o2.getLastChatmessageDto().getTime()).withNano(0).compareTo(LocalDateTime.parse(o1.getLastChatmessageDto().getTime()).withNano(0));
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(chatRoomListGetResponseList,comparator);
    }

}

