package com.catchroom.chat.chatroom.entity;

import com.catchroom.chat.message.type.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom implements Serializable {


    private Long id;
    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private String chatRoomNumber;
    private UserIdentity loginUserIdentity;

}
