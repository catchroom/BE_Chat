package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.repository.ChatRoomRedisRepository;
import com.catchroom.chat.message.type.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {
    private final MainFeignClient mainFeignClient;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatRoomListGetResponse> getChatRoomListAccessToken(String accessToken) {

        List<ChatRoomListGetResponse> chatRoomList = mainFeignClient.getChatRoomList(accessToken);
        chatRoomList.forEach(this::recallLastMessage);
        sortChatRoomListLatest(chatRoomList);

        return chatRoomList;
    }

    public List<ChatRoomListGetResponse> getChatRoomList(
            Long userId, String accessToken, MessageType type
    ) {
        Long beforeTime = System.currentTimeMillis();
        List<ChatRoomListGetResponse> chatRoomList = chatRoomRedisRepository.getChatRoomList(userId);
        if (type.equals(MessageType.DELETE) || type.equals(MessageType.ENTER) || chatRoomList == null) {

            chatRoomList = mainFeignClient.getChatRoomList(accessToken);
            chatRoomRedisRepository.setChatRoomList(userId, chatRoomList);

            log.info("NOT ChatRoom!!!!! time : {}", System.currentTimeMillis() - beforeTime);

        } else {

            log.info("ChatRoom have!!!! time : {}", System.currentTimeMillis() - beforeTime);

        }

        chatRoomList.forEach(this::recallLastMessage);
        sortChatRoomListLatest(chatRoomList);
        return chatRoomList;
    }

    private void recallLastMessage(ChatRoomListGetResponse chatRoomListGetResponse) {

        String chatRoomNumber = chatRoomListGetResponse.getChatRoomNumber();

        //최신 메세지가 레디스에 있는 경우
        if (chatRoomRedisRepository.getLastMessage(chatRoomNumber) != null) {
            chatRoomListGetResponse.updateChatMessageDto(chatRoomRedisRepository.getLastMessage(chatRoomNumber));
        }

        //최신 메세지가 레디스에 없고 몽고디비에는 있는 경우
        else if (chatRoomRedisRepository.getLastMessage(chatRoomNumber) == null &&
            !chatMessageRepository.findAllByRoomId(chatRoomNumber).isEmpty()) {
            ChatMessage lastChatMessageMongo = chatMessageRepository.findAllByRoomId(chatRoomNumber).get(0);
            chatRoomListGetResponse.updateChatMessageDto(ChatMessageDto.fromEntity(lastChatMessageMongo));
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
