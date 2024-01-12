package com.catchroom.chat.chatroom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom implements Serializable {

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
