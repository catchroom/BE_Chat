package com.catchroom.chat.message.service;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.entity.ChatMessage;
import com.catchroom.chat.message.repository.ChatMessageRepository;
import com.catchroom.chat.message.type.MessageType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMongoService {

    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

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
    public List<ChatMessageDto> findAll(String roomId,Integer pageNumber) {
        return findByRoomIdWithPaging(roomId,pageNumber,20)
                        .stream().map(ChatMessageDto::fromEntity)
                        .collect(Collectors.toList());
    }


    private Page<ChatMessage> findByRoomIdWithPaging(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"time"));

        Query query = new Query()
            .with(pageable)
            .skip((long) pageable.getPageSize() * pageable.getPageNumber())
            .limit(pageable.getPageSize());

        query.addCriteria(Criteria.where("roomId").is(roomId));

        List<ChatMessage> filteredChatMessage = mongoTemplate.find(query, ChatMessage.class, "chat");
        return PageableExecutionUtils.getPage(
            filteredChatMessage,
            pageable,
            () -> mongoTemplate.count(query.skip(-1).limit(-1), ChatMessage.class)
        );
    }



}
