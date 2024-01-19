package com.catchroom.chat.message.dto;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class MessageSubDto implements Serializable {
    private ChatMessageDto chatMessageDto;
    private List<ChatRoomListGetResponse> list;
}
