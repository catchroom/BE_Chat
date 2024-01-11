package com.catchroom.chat.controller;

import com.catchroom.chat.dto.ChatMessageDto;
import com.catchroom.chat.service.ChatMongoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatFindController {
    private final ChatMongoService chatMongoService;
    @GetMapping("/room/find/{roomId}")
    public List<ChatMessageDto> roomInfo(@PathVariable String roomId) {
        return chatMongoService.findAll(roomId);
    }
}
