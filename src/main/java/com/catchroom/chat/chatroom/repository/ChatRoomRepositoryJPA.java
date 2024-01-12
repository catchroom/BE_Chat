package com.catchroom.chat.chatroom.repository;

import com.catchroom.chat.chatroom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepositoryJPA extends JpaRepository<ChatRoom,Long> {
    List<ChatRoom> findAllByBuyerId(Long buyerId);
    List<ChatRoom> findAllBySellerId(Long sellerId);

}
