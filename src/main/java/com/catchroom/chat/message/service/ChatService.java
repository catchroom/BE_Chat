package com.catchroom.chat.message.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.global.common.SuccessMessage;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.global.pubsub.RedisPublisher;
import com.catchroom.chat.message.dto.MessageSubDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.repository.ChatRoomRedisRepository;
import com.catchroom.chat.message.type.MessageType;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
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
        chatRoomRedisRepository.setLastChatMessage(chatMessage.getRoomId(), chatMessage);

        Long before = System.currentTimeMillis();

        // 1. 마지막 메시지들이 담긴 채팅방 리스트들을 가져온다.
        List<ChatRoomListGetResponse> chatRoomListGetResponseList = chatRoomService.getChatRoomList(
                chatMessage.getUserId(), accessToken
        );

        // 2. 채팅방 리스트에 새로운 채팅방 정보가 없다면, 넣어준다. 마지막 메시지도 같이 담는다.
        setNewChatRoomInfo(chatMessage, accessToken);

        // 3. 채팅방이 삭제되는 것이라면 delete 를 해준다.
        if (chatMessage.getType().equals(MessageType.DELETE)) {
            chatRoomService.deleteChatRoom(accessToken, chatMessage.getRoomId(), chatMessage.getUserId());
        }

        // 4. 마지막 메세지 기준으로 정렬 채팅방 리스트 정렬
        chatRoomService.sortChatRoomListLatest(chatRoomListGetResponseList);

        MessageSubDto messageSubDto = MessageSubDto.builder()
            .chatMessageDto(chatMessage)
            .list(chatRoomListGetResponseList)
            .build();

        Long after = System.currentTimeMillis() - before;
        log.info("message Time : {}", after);

        redisPublisher.publish(messageSubDto);
    }


    /**
     * redis 에 채팅방 정보가 없는 경우 새로 저장.
     * @param chatMessage
     * @param accessToken
     */
    private void setNewChatRoomInfo(ChatMessageDto chatMessage, String accessToken) {

        ChatRoomListGetResponse newChatRoomListResponse = null;

        //레디스에 해당 채팅방이 저장이 안되어 있다면 추가해준다.
        if (!chatRoomRedisRepository.existChatRoom(chatMessage.getUserId(), chatMessage.getRoomId())) {
            newChatRoomListResponse = chatRoomService.getChatRoomInfo(accessToken, chatMessage.getRoomId());
        } else {
            newChatRoomListResponse = chatRoomRedisRepository.getChatRoom(chatMessage.getUserId(), chatMessage.getRoomId());
        }

        newChatRoomListResponse.updateChatMessageDto(chatMessage);

        chatRoomRedisRepository.setChatRoom(chatMessage.getUserId(), chatMessage.getRoomId(), newChatRoomListResponse);

    }






}
