package com.catchroom.chat.entity;

import com.catchroom.chat.dto.ChatMessageDto;
import com.catchroom.chat.type.MessageType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@AllArgsConstructor
@Document(collection = "chat")
public class ChatMessage {

//    @Id
//    private Long id;

    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 메시지
    private String time; // 메시지 보낸 시간

    public static ChatMessage of(ChatMessageDto dto) {
        return ChatMessage.builder()
                .type(dto.getType())
                .roomId(dto.getRoomId())
                .sender(dto.getSender())
                .message(dto.getMessage())
                .time(LocalDateTime.now().toString())
                .build();
    }
}

