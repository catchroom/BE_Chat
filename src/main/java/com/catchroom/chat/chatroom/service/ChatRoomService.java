package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final MainFeignClient mainFeignClient;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    public List<ChatRoomListGetResponse> getChatRoomList(String accessToken) {
        List<ChatRoomListGetResponse> chatRoomListGetResponseList = mainFeignClient.getChatRoomList(accessToken);
        for (ChatRoomListGetResponse chatRoomListGetResponse : chatRoomListGetResponseList) {
            chatRoomListGetResponse.updateChatMessageDto(chatRoomRepository.getLastMessage(chatRoomListGetResponse.getChatRoomNumber()));
//            if (chatRoomRepository.getLastMessage(chatRoomListGetResponse.getChatRoomNumber()) == null) {
//                ChatMessage lastChatMessage = chatMessageRepository.findAllByRoomId(chatRoomListGetResponse.getChatRoomNumber()).get(0);
//                chatRoomListGetResponse.updateChatMessageDto(ChatMessageDto.fromEntity(lastChatMessage));
//            }
        }
        return chatRoomListGetResponseList;
    }
}
