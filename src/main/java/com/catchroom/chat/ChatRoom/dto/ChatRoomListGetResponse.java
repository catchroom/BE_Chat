package com.catchroom.chat.ChatRoom.dto;

import com.catchroom.chat.ChatRoom.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListGetResponse {
    private List<ChatRoom> ChatRoomListUserIsBuyer;
    private List<ChatRoom> ChatRoomListUserIsSeller;

    public static ChatRoomListGetResponse of(List<ChatRoom> ChatRoomListUserIsBuyer, List<ChatRoom> ChatRoomListUserIsSeller) {
        return ChatRoomListGetResponse.builder()
            .ChatRoomListUserIsBuyer(ChatRoomListUserIsBuyer)
            .ChatRoomListUserIsSeller(ChatRoomListUserIsSeller)
            .build();
    }
}
