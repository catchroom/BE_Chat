package com.catchroom.chat.ChatRoom.service;

import com.catchroom.chat.ChatRoom.dto.ChatRoomCreateRequest;
import com.catchroom.chat.ChatRoom.dto.ChatRoomCreateResponse;
import com.catchroom.chat.ChatRoom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.ChatRoom.entity.ChatRoom;
import com.catchroom.chat.ChatRoom.repository.ChatRoomRepositoryJPA;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepositoryJPA chatRoomRepository;
    @Resource(name = "redisTemplate") //redisTemplate bean 주입.
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM_REDIS";

    @Transactional(readOnly = true)
    public ChatRoomListGetResponse findChatRoomListByMemberId(Long userId) {
        List<ChatRoom> ChatRoomListUserIsBuyer = chatRoomRepository.findAllByBuyerId(userId);

        List<ChatRoom> ChatRoomListUserIsSeller = chatRoomRepository.findAllBySellerId(userId);
        return ChatRoomListGetResponse.of(ChatRoomListUserIsBuyer, ChatRoomListUserIsSeller);
    }

    @Transactional
    public ChatRoomCreateResponse createChatRoom(ChatRoomCreateRequest chatRoomCreateRequest) {
        ChatRoom chatRoom = ChatRoom.create(
            chatRoomCreateRequest.getSellerId(),
            chatRoomCreateRequest.getBuyerId(),
            chatRoomCreateRequest.getProductId());
        chatRoomRepository.save(chatRoom);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getChatRoomNumber(), chatRoom);
        return ChatRoomCreateResponse.fromEntity(chatRoom);
    }
}
