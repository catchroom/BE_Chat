package com.catchroom.chat.message.controller;

import com.catchroom.chat.global.common.ApiResponse;
import com.catchroom.chat.message.service.ChatMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatFindController {
    private final ChatMongoService chatMongoService;
    @GetMapping("/room/find")
    public ResponseEntity<?> roomFindInfo(
            @RequestParam(name = "id") String id
    ) {
        log.info("ChatFindController : {}", id);
        return ResponseEntity.ok(
                ApiResponse.create(6003, chatMongoService.findAll(id))
        );
    }

//    @DeleteMapping("/room")
//    public ResponseEntity<?> roomDelete(
//            @RequestParam(name = "id") String id
//    ) {
//        chatMongoService.deleteRoomId(id);
//        return ResponseEntity.ok(
//                ApiResponse.create(6004, id + " 삭제 완료")
//        );
//    }
}
