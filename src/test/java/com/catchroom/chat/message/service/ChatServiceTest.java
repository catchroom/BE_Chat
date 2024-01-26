package com.catchroom.chat.message.service;

import static org.junit.jupiter.api.Assertions.*;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.type.MessageType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Autowired
    ChatMongoService chatMongoService;

    @Test
    void 실행_성공_토큰_재작성_필수() {
        String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7KeA7Jq0IiwicGhvbmVOdW1iZXIiOiIwMTAwMDAwMDAwMCIsIm5pY2tOYW1lIjoi7LGE7YyF7ISc67KEIiwiZW1haWwiOiJ0ZXN0MTEyMjExMjJAbWFpbC5jb20iLCJpc3MiOiJjYXRjaHJvb20iLCJpYXQiOjE3MDYyODQyNjIsImV4cCI6MTcwNjI4NjA2MX0.lge6IkfJAUr00HE47U5fdJpB5UtQUu9u8PKyKOLL7lS3gX8ORIxV8j0IM8xpfpO9fcq_hq7nP45YOw8yGX0kQw";
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .type(MessageType.TALK)
                .message("실행돼면 좋겠다 제발 ~!")
                .roomId("37bd6d8f-4b63-44bc-a1ec-26deea91e06d")
                .userId(69L)
                .build();
        ChatMessageDto dto = chatMongoService.save(messageDto);
        chatService.sendChatMessage(dto, accessToken);


    }
}