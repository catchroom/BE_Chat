package com.catchroom.chat.chatroom.controller;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.global.common.ApiResponse;
import com.catchroom.chat.message.dto.AccommodationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chat/room")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //TODO 토큰 헤더에 담을 것
    @GetMapping("/list")
    public ChatRoomListGetResponse getChatRoomList() {
        return chatRoomService.getChatRoomList();
    }
}
