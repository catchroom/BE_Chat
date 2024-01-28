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
        String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7KeA7Jq0IiwicGhvbmVOdW1iZXIiOiIwMTAwMDAwMDAwMCIsIm5pY2tOYW1lIjoi7LGE7YyF7ISc67KEIiwiZW1haWwiOiJ0ZXN0MTEyMjExMjJAbWFpbC5jb20iLCJpc3MiOiJjYXRjaHJvb20iLCJpYXQiOjE3MDYzMzIyNTcsImV4cCI6MTcwNjMzNDA1Nn0.kLyPKJSUqCFAHu-spGM8Rpf-Y30zEm4Gxm0IXxLg30c0-2rpWn8nw6b8h1TAvkQUy5T3ttTtLsZsD6V4wTlDRQ";

        ChatMessageDto messageDto = ChatMessageDto.builder()
                .type(MessageType.TALK)
                .message("실행돼면 좋겠다 제발요... ㅠㅠ")
                .roomId("555-666-3jd")
                .userId(10000L)
                .build();
        ChatMessageDto dto = chatMongoService.save(messageDto);
        chatService.sendChatMessage(dto, accessToken);


    }
}