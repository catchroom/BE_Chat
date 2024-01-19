package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.entity.ChatRoom;
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
        List<ChatRoomListGetResponse> chatRoomList = mainFeignClient.getChatRoomList(accessToken);
        for (ChatRoomListGetResponse chatRoomListGetResponse : chatRoomList) {
            recallLastMessage(chatRoomListGetResponse);
        }
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
}
