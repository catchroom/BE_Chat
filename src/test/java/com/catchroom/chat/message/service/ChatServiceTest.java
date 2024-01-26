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

    @Test
    void 실행_성공_토큰_재작성_필수() {
        String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi6rmA7LKg7IiYIiwicGhvbmVOdW1iZXIiOiIwMTA4ODI1NDQ3MSIsIm5pY2tOYW1lIjoi7LKg7IiY7JmA7JiB7Z2sIiwiZW1haWwiOiJ0ZXN0OTI3QG5hdmVyLmNvbSIsImlzcyI6ImNhdGNocm9vbSIsImlhdCI6MTcwNjI2MDc3NSwiZXhwIjoxNzA2MjYyNTc0fQ.hd4zTCpvmTjVROOGOwnKumWMUjW-lKlDJhSML79Fc2MUEDzeXSZBYAznFV1tolhNdl2qrA1N8HgT1tchhKsx2A";
        ChatMessageDto messageDto = ChatMessageDto.builder()
                .type(MessageType.TALK)
                .message("실행되니? 3탄")
                .roomId("f5556003-1151-4d26-a3a8-346db5e622a3")
                .userId(28L)
                .time(LocalDateTime.now().toString())
                .build();

        chatService.sendChatMessage(messageDto, accessToken);

    }
}