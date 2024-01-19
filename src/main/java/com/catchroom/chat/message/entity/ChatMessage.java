package com.catchroom.chat.message.entity;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.type.MessageType;
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

    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private Long userId; // 채팅 보낸 userId
    private String message; // 메시지
    private String time; // 메시지 보낸 시간
    private int negoPrice;

    public static ChatMessage of(ChatMessageDto dto) {
        return ChatMessage.builder()
                .type(dto.getType())
                .roomId(dto.getRoomId())
                .sender(dto.getSender())
                .userId(dto.getUserId())
                .message(dto.getMessage())
                .time(LocalDateTime.now().toString())
                .negoPrice(dto.getNegoPrice())
                .build();
    }
}

