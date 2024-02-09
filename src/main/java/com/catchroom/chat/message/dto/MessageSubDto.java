package com.catchroom.chat.message.dto;

import com.catchroom.chat.chatroom.dto.ChatRoomGetResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class MessageSubDto implements Serializable {
    private Long userId;
    private Long partnerId;
    private ChatMessageDto chatMessageDto;
    private List<ChatRoomGetResponse> list;
    private List<ChatRoomGetResponse> partnerList;
}
