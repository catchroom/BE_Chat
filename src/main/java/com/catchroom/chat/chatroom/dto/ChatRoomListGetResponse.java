package com.catchroom.chat.chatroom.dto;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.type.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListGetResponse {
    private String chatRoomNumber;
    private Long buyerId;
    private Long sellerId;
    private Long productId;
    private UserIdentity loginUserIdentity;
    private String accommodationUrl;
    private String partnerNickName;
    private ChatMessageDto lastChatmessageDto;

    public void updateChatMessageDto(ChatMessageDto chatMessageDto) {
        this.lastChatmessageDto = chatMessageDto;
    }
}
