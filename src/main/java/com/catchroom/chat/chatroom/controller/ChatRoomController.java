package com.catchroom.chat.chatroom.controller;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/chat/room")
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://localhost:3000","https://dev.dhlbrqe2v28e4.amplifyapp.com"}, allowedHeaders = "*")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //TODO 같은게 2개..? getChatRoomList에 모든 정보가 다 담기는데 왜??

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
