package com.catchroom.chat.message.dto;

import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.type.MessageType;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto implements Serializable {
    // 메시지  타입 : 입장, 채팅, 퇴장

    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private Long userId;
    private String message; // 메시지
    private String time;
    private long userCount; // 채팅방 인원 수
    private int negoPrice; //네고 가격

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .type(chatMessage.getType())
                .sender(chatMessage.getSender())
                .userId(chatMessage.getUserId())
                .roomId(chatMessage.getRoomId())
                .time(chatMessage.getTime())
                .message(chatMessage.getMessage())
                .negoPrice(chatMessage.getNegoPrice())
                .build();
    }

}
