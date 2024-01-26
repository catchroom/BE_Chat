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
        String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7LGE7YyF7Y287YK5IiwicGhvbmVOdW1iZXIiOiIwMTAwMDAwMDAwMCIsIm5pY2tOYW1lIjoi7LGE7YyF7Y287YK5IiwiZW1haWwiOiJjaGF0ZnVja0BtYWlsLmNvbSIsImlzcyI6ImNhdGNocm9vbSIsImlhdCI6MTcwNjI5MDUxNSwiZXhwIjoxNzA2MjkyMzE0fQ.Z_sAr_3GwY-mDcr7WPrg3nhscEqbje_Jt2kA2xhSko9OPCndEPEDzURqS9oYMrw_Yc4mEGiJFpst-cFDgpvMpQ";

        ChatMessageDto messageDto = ChatMessageDto.builder()
                .type(MessageType.TALK)
                .message("실행돼면 좋겠다 제발요... ㅠㅠ")
                .roomId("37bd6d8f-4b63-44bc-a1ec-26deea91e06d")
                .userId(69L)
                .build();
        ChatMessageDto dto = chatMongoService.save(messageDto);
        chatService.sendChatMessage(dto, accessToken);


    }
}