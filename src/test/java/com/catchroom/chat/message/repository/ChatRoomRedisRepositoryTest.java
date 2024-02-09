package com.catchroom.chat.message.repository;

import com.catchroom.chat.chatroom.dto.ChatRoomGetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatRoomRedisRepositoryTest {

    @Autowired
    private ChatRoomRedisRepository chatRoomRedisRepository;

    @Autowired
    ObjectMapper objectMapper;

    Logger log = (Logger) LoggerFactory.getLogger(ChatRoomRedisRepositoryTest.class);

    @Test
    void 채팅방_값_저장() {
        Long userId = 68L;
    }

    @Test
    void 채팅방_값이_제대로_저장됐을까() {
        Long userId = 10000L;
        List<ChatRoomGetResponse> chatRoomListGetResponseList = chatRoomRedisRepository.getChatRoomList(userId);
        for (ChatRoomGetResponse chatRoomListGetResponse : chatRoomListGetResponseList) {
            log.info(
                    chatRoomListGetResponse.toString()
            );
        }
    }

    @Test
    void 채팅방_값이_제대로_불러와질까() {
        Long userId = 10000L;
        ChatRoomGetResponse chatRoomListGetResponse = chatRoomRedisRepository.getChatRoom(userId, "c29cd80e-d289-49ff-ac91-f3b32dd4b633");
        log.info(chatRoomListGetResponse.toString());
    }
}