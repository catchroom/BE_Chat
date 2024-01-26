package com.catchroom.chat.chatroom.controller;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.message.type.MessageType;
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
        @RequestHeader("Authorization") String accessToken,
        @RequestParam(name = "userId") Long userId
    ) {
        return chatRoomService.getChatRoomListByHttp(userId, accessToken);
    }


    @GetMapping("/info")
    public ChatRoomListGetResponse getChatRoom(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam(name = "roomId") String roomId
    ) {
        return chatRoomService.getChatRoomInfo(accessToken, roomId);
    }



}
