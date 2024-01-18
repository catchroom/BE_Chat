package com.catchroom.chat.message.service;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.type.MessageType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMongoService {

    private final ChatMessageRepository chatMessageRepository;

    // 채팅 저장
    @Transactional
    public ChatMessageDto save(ChatMessageDto chatMessageDto) {
        if (chatMessageDto.getType() == MessageType.ENTER ||
            chatMessageDto.getType() == MessageType.TALK ||
            chatMessageDto.getType() == MessageType.QUIT) {
            chatMessageDto.setNegoPrice(-1);
        }
        ChatMessage chatMessage = chatMessageRepository.save(ChatMessage.of(chatMessageDto));
        log.info("save success : {}", chatMessage.getMessage());
        return ChatMessageDto.fromEntity(chatMessage);
    }

    // 채팅 불러오기
    @Transactional(readOnly = true)
    public List<ChatMessageDto> findAll(String roomId) {
        List<ChatMessageDto> chatMessageList =
                chatMessageRepository.findAllByRoomId(roomId)
                        .stream().map(ChatMessageDto::fromEntity)
                        .collect(Collectors.toList());
        return chatMessageList;
    }

    @Transactional
    public void deleteRoomId(String roomId) {
        chatMessageRepository.deleteAllByRoomId(roomId);
    }



}
