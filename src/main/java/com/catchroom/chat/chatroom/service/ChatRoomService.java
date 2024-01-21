package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.entity.ChatRoom;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    public List<ChatRoomListGetResponse> getChatRoomList(String accessToken) {
        List<ChatRoomListGetResponse> chatRoomList = mainFeignClient.getChatRoomList(accessToken);
        chatRoomList.forEach(this::recallLastMessage);
        sortChatRoomListLatest(chatRoomList);
        return chatRoomList;
    }

    private void recallLastMessage(ChatRoomListGetResponse chatRoomListGetResponse) {
        String chatRoomNumber = chatRoomListGetResponse.getChatRoomNumber();
        //최신 메세지가 레디스에 있는 경우
        if (chatRoomRepository.getLastMessage(chatRoomNumber) != null) {
            chatRoomListGetResponse.updateChatMessageDto(chatRoomRepository.getLastMessage(chatRoomNumber));
        }
        //최신 메세지가 레디스에 없고 몽고디비에는 있는 경우
        else if (chatRoomRepository.getLastMessage(chatRoomNumber) == null &&
            !chatMessageRepository.findAllByRoomId(chatRoomNumber).isEmpty()) {
            ChatMessage lastChatMessageMongo = chatMessageRepository.findAllByRoomId(chatRoomNumber).get(0);
            chatRoomListGetResponse.updateChatMessageDto(ChatMessageDto.fromEntity(lastChatMessageMongo));
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
