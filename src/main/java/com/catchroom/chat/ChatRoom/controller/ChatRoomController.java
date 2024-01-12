package com.catchroom.chat.ChatRoom.controller;

import com.catchroom.chat.ChatRoom.dto.ChatRoomCreateRequest;
import com.catchroom.chat.ChatRoom.entity.ChatRoom;
import com.catchroom.chat.ChatRoom.service.ChatRoomService;
import com.catchroom.chat.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    @GetMapping("/{userId}")
    public ResponseEntity<?> findChatRoomListByMemberId(@PathVariable Long userId) {
        return ResponseEntity.ok(
            ApiResponse.create(6000, chatRoomService.findChatRoomListByMemberId(userId)));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {
        return ResponseEntity.ok(
            ApiResponse.create(6002,chatRoomService.createChatRoom(chatRoomCreateRequest)));
    }
}
