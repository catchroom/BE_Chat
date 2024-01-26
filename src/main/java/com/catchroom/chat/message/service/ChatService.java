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
    private final ChatMessageRepository chatMessageRepository;
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

        Long before = System.currentTimeMillis();

        // 1. redis 에 채팅방이 없으면, 레디스에 리스트를 먼저 넣어준다.
        initIfChatRoomAbsent(chatMessage, accessToken);

        // 2. 새로운 채팅방 정보가 없다면, 넣어준다.
        setNewChatRoomInfo(chatMessage, accessToken);

        // 3. 채팅방에 마지막 메시지를 넣어준다.
        setRedisChatMessage(chatMessage);

        // 4. 채팅방이 삭제되는 것이라면 delete를 해준다.
        if (chatMessage.getType().equals(MessageType.DELETE)) {
            chatRoomRedisRepository.deleteChatRoom(chatMessage.getUserId(), chatMessage.getRoomId());
            deleteChatRoom(accessToken, chatMessage.getRoomId());
        }

        // 5. 채팅방 정보 업데이트
        List<ChatRoomListGetResponse> chatRoomListGetResponseList =
            chatRoomRedisRepository.getChatRoomList(chatMessage.getUserId());

        // 6. 마지막 메세지 기준으로 정렬
        sortChatRoomListLatest(chatRoomListGetResponseList);

        MessageSubDto messageSubDto = MessageSubDto.builder()
            .chatMessageDto(chatMessage)
            .list(chatRoomListGetResponseList)
            .build();

        Long after = System.currentTimeMillis() - before;
        log.info("message Time : {}", after);

        redisPublisher.publish(messageSubDto);
    }

    /**
     * 채팅방 리스트 정보 업데이트
     * @param chatMessage
     * @param accessToken
     */
    private void initIfChatRoomAbsent(ChatMessageDto chatMessage, String accessToken) {
        if (!chatRoomRedisRepository.existChatRoomList(chatMessage.getUserId())) {
            List<ChatRoomListGetResponse> list =
                    chatRoomService.getChatRoomListAccessToken(accessToken);

            setLastMessage(list);

            log.error("init message : {}, userId : {}", chatMessage.getMessage(), chatMessage.getUserId());
            chatRoomRedisRepository.initChatRoomList(chatMessage.getUserId(), list);
        }
    }

    /**
     * redis에 채팅방 정보가 없는 경우 새로 저장.
     * @param chatMessage
     * @param accessToken
     */
    private void setNewChatRoomInfo(ChatMessageDto chatMessage, String accessToken) {
        if (!chatRoomRedisRepository.existChatRoom(chatMessage.getUserId(), chatMessage.getRoomId())) {
            ChatRoomListGetResponse newChatRoom = mainFeignClient.getChatRoomInfo(
                    accessToken, chatMessage.getRoomId()
            );

            chatRoomRedisRepository.setChatRoom(
                    chatMessage.getUserId(), chatMessage.getRoomId(), newChatRoom
            );
        }
    }

    /**
     * 채팅방 마지막 메시지 업데이트 하는 곳
     * @param chatMessage
     */
    private void setRedisChatMessage(ChatMessageDto chatMessage) {
        ChatRoomListGetResponse chatRoomListGetResponse =
                chatRoomRedisRepository.getChatRoom(chatMessage.getUserId(), chatMessage.getRoomId());

        chatRoomListGetResponse.updateChatMessageDto(chatMessage);

        chatRoomRedisRepository.setChatRoom(
                chatMessage.getUserId(), chatMessage.getRoomId(), chatRoomListGetResponse
        );
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

    /**
     * 몽고 디비에서 마지막 메시지 가져와서 저장하는 로직
     * @param chatRoomListGetResponseList
     */
    private void setLastMessage(List<ChatRoomListGetResponse> chatRoomListGetResponseList) {

        // 몽고 디비에서 마지막 메시지 가져와서 저장.

        for (ChatRoomListGetResponse chatRoomListGetResponse : chatRoomListGetResponseList) {

            String chatRoomNumber = chatRoomListGetResponse.getChatRoomNumber();

            if (chatRoomListGetResponse.getLastChatmessageDto() == null) {
                if (chatMessageRepository.findAllByRoomId(chatRoomNumber) != null) {
                    ChatMessage lastChatMessageMongo = chatMessageRepository.findAllByRoomId(
                            chatRoomNumber).get(0);
                    chatRoomListGetResponse.updateChatMessageDto(
                            ChatMessageDto.fromEntity(lastChatMessageMongo));
                }
            }
        }

    }

    /**
     * 채팅방 마지막 메시지의 시간들을 비교하여 정렬하는 메소드
     * @param chatRoomListGetResponseList
     */
    private void sortChatRoomListLatest (
            List<ChatRoomListGetResponse> chatRoomListGetResponseList
    ) {
        Comparator<ChatRoomListGetResponse> comparator = (o1, o2) -> {

            if (o1.getLastChatmessageDto() != null && o2.getLastChatmessageDto() != null) {
                return LocalDateTime.parse(o2.getLastChatmessageDto().getTime()).withNano(0)
                        .compareTo(
                                LocalDateTime.parse(o1.getLastChatmessageDto().getTime()).withNano(0)
                        );
            } else {
                return 0;
            }
        };

        Collections.sort(chatRoomListGetResponseList,comparator);
    }

}
