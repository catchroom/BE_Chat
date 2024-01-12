package com.catchroom.chat.chatroom.dto;

import com.catchroom.chat.chatroom.entity.ChatRoom;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCreateRequest {

    private Long buyerId;
    private Long sellerId;
    private Long productId;

    public ChatRoom toEntity() {
        return ChatRoom.builder()
            .buyerId(buyerId)
            .sellerId(sellerId)
            .productId(productId)
            .build();
    }
}
