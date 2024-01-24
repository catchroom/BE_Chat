package com.catchroom.chat.chatroom.controller;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/chat/room")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:3000","https://dev.dhlbrqe2v28e4.amplifyapp.com"}, allowedHeaders = "*")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    public List<ChatRoomListGetResponse> getChatRoomList(
        @RequestHeader("Authorization") String accessToken
        ) {
        return chatRoomService.getChatRoomList(accessToken);
    }


}
