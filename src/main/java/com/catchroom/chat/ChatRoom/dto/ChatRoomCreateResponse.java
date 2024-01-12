package com.catchroom.chat.ChatRoom.dto;

import com.catchroom.chat.ChatRoom.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCreateResponse {
    private String ChatRoomNumber;

    public static ChatRoomCreateResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomCreateResponse.builder()
            .ChatRoomNumber(chatRoom.getChatRoomNumber())
            .build();
    }
}
