package com.catchroom.chat.message.controller;

import com.catchroom.chat.global.common.ApiResponse;
import com.catchroom.chat.message.service.ChatMongoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
@CrossOrigin(origins = {"https://localhost:3000","https://dev.dhlbrqe2v28e4.amplifyapp.com"}, allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class ChatFindController {
    private final ChatMongoService chatMongoService;

    @GetMapping("/room/find")
    public ResponseEntity<?> roomFindInfo(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam(name = "id") String id
    ) {
        return ResponseEntity.ok(
                ApiResponse.create(6003, chatMongoService.findAll(id))
        );
    }

}
