package com.catchroom.chat.ChatRoom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private String chatRoomNumber;

    public ChatRoom(Long sellerId, Long buyerId, Long productId, String chatRoomNumber) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.productId = productId;
        this.chatRoomNumber = chatRoomNumber;
    }

    public static ChatRoom create(Long sellerId, Long buyerId, Long productId) {
        String chatRoomNumber = UUID.randomUUID().toString();
        ChatRoom chatRoom = new ChatRoom(sellerId,buyerId,productId,chatRoomNumber);
        return chatRoom;
    }

}
