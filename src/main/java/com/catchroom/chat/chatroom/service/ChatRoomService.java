package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.message.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final MainFeignClient mainFeignClient;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomListGetResponse> getChatRoomList(String accessToken) {
        List<ChatRoomListGetResponse> chatRoomListGetResponseList = mainFeignClient.getChatRoomList(accessToken);
        for (ChatRoomListGetResponse chatRoomListGetResponse : chatRoomListGetResponseList) {
            chatRoomListGetResponse.updateChatMessageDto(chatRoomRepository.getLastMessage(chatRoomListGetResponse.getChatRoomNumber()));
        }
        return chatRoomListGetResponseList;
    }
}
