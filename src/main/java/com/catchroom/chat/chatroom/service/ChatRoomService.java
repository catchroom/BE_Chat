package com.catchroom.chat.chatroom.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.entity.ChatRoom;
import com.catchroom.chat.feign.client.MainFeignClient;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final MainFeignClient mainFeignClient;
    @Resource(name = "redisTemplate") //redisTemplate bean 주입.
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private static final String CHAT_ROOMS = "CHAT_ROOM_REDIS";


    public List<ChatRoomListGetResponse> getChatRoomList(String accessToken) {

        return mainFeignClient.getChatRoomList(accessToken);
    }
}
