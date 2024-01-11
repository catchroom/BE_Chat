package com.catchroom.chat.repository;

import com.catchroom.chat.entity.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {

    List<ChatMessage> findAllByRoomId(String roomId);
}

