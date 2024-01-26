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
        String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7KeA7Jq0IiwicGhvbmVOdW1iZXIiOiIwMTAwMDAwMDAwMCIsIm5pY2tOYW1lIjoi7LGE7YyF7ISc67KEIiwiZW1haWwiOiJ0ZXN0MTEyMjExMjJAbWFpbC5jb20iLCJpc3MiOiJjYXRjaHJvb20iLCJpYXQiOjE3MDYyOTM4MDgsImV4cCI6MTcwNjI5NTYwN30.agUOG-xMEhlT-JXXO0ViTTXgG8ZAs6rilM2xYdN0G73dFHN2yFJx5_vb9DQbb0ymM27xLarDi6I7pnd3JIx1Rw";

        ChatMessageDto messageDto = ChatMessageDto.builder()
                .type(MessageType.TALK)
                .message("실행돼면 좋겠다 제발요... ㅠㅠ")
                .roomId("37bd6d8f-4b63-44bc-a1ec-26deea91e06d")
                .userId(68L)
                .build();
        ChatMessageDto dto = chatMongoService.save(messageDto);
        chatService.sendChatMessage(dto, accessToken);


    }
}