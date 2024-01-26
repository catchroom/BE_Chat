package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.global.common.SuccessMessage;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.repository.ChatRoomRedisRepository;
import com.catchroom.chat.message.service.ChatMongoService;
import com.catchroom.chat.message.service.ChatService;
import com.catchroom.chat.message.type.MessageType;
import java.util.ArrayList;
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
    private final ChatMongoService chatMongoService;

    public ChatRoomListGetResponse getChatRoomInfo(String accessToken, String roomId) {
        return mainFeignClient.getChatRoomInfo(accessToken, roomId);
    }

    public List<ChatRoomListGetResponse> getChatRoomListByHttp(Long userId, String accessToken) {
        // 처음 HTTP 요청에서는 무조건 레디스 초기화 진행하도록 로직 수정
        List<ChatRoomListGetResponse> chatRoomListGetResponseList = mainFeignClient.getChatRoomList(accessToken);
        chatRoomRedisRepository.initChatRoomList(userId, chatRoomListGetResponseList);
        sortChatRoomListLatest(chatRoomListGetResponseList);
        return chatRoomListGetResponseList;
    }

    public List<ChatRoomListGetResponse> getChatRoomListByUserId(Long userId) {
        List<ChatRoomListGetResponse> chatRoomListGetResponseList = new ArrayList<>();

        if (chatRoomRedisRepository.existChatRoomList(userId)) {
            chatRoomListGetResponseList = chatRoomRedisRepository.getChatRoomList(userId);
            chatRoomListGetResponseList.forEach(this::setListChatLastMessage);
        }

        return chatRoomListGetResponseList;
    }


    public List<ChatRoomListGetResponse> getChatRoomList(Long userId, String accessToken) {
        Long beforeTime = System.currentTimeMillis();

        List<ChatRoomListGetResponse> chatRoomListGetResponseList = null;
        if (chatRoomRedisRepository.existChatRoomList(userId)) {
            chatRoomListGetResponseList = chatRoomRedisRepository.getChatRoomList(userId);

            log.info("YES ChatRoom!!!!! time : {}", System.currentTimeMillis() - beforeTime);

        } else {
            // 채팅방이 레디스에 없으면 페인 사용해서 불러온다!
            chatRoomListGetResponseList = mainFeignClient.getChatRoomList(accessToken);
            chatRoomRedisRepository.initChatRoomList(userId, chatRoomListGetResponseList);
            log.info("NOT ChatRoom!!!!! time : {}", System.currentTimeMillis() - beforeTime);
        }

        chatRoomListGetResponseList.forEach(this::setListChatLastMessage);

        log.info("Total Room List time : {}", System.currentTimeMillis() - beforeTime);

        return chatRoomListGetResponseList;
    }


    /**
     * 몽고 디비에서 마지막 메시지 가져와서 저장하는 로직
     * @param chatRoomListGetResponse
     */
    private void setListChatLastMessage(ChatRoomListGetResponse chatRoomListGetResponse) {

        // 몽고 디비에서 마지막 메시지 가져와서 저장.
        String chatRoomNumber = chatRoomListGetResponse.getChatRoomNumber();

        if (chatRoomRedisRepository.getLastMessage(chatRoomNumber) != null) {
            chatRoomListGetResponse.updateChatMessageDto(
                    chatRoomRedisRepository.getLastMessage(chatRoomNumber)
            );
            return;
        }

        ChatMessage chatMessage = chatMongoService.findLatestMessageByRoomId(chatRoomNumber);
        if (chatMessage != null) {
            chatRoomListGetResponse.updateChatMessageDto(
                    ChatMessageDto.fromEntity(chatMessage)
            );
        }

    }

    /**
     * 채팅방 마지막 메시지의 시간들을 비교하여 정렬하는 메소드
     * @param chatRoomListGetResponseList
     */
    public void sortChatRoomListLatest (
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

    /**
     * 채팅방 삭제 로직
     * @param accessToken
     * @param roomId
     */
    public void deleteChatRoom(String accessToken, String roomId, Long userId) {
        log.info("=>> 채팅방 삭제 {} start ", roomId);
        SuccessMessage message = mainFeignClient.deleteChatRoom(accessToken, roomId);
        chatRoomRedisRepository.deleteChatRoom(userId, roomId);
        log.info("=>> 채팅방 삭제 {} Msg : {}", roomId, message.Meassage());
    }


}
