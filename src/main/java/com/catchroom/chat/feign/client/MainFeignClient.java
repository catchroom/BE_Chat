package com.catchroom.chat.feign.client;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.global.config.FeignConfig;
import com.catchroom.chat.message.dto.AccommodationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 채팅 서버 입장 (시큐리티 X)
 * userId로 user 정보를 가져오는 상황
 * 비로그인 사용자도 접근할 수 있음.
 *
 *
 */
@FeignClient(
        name = "mainFeign", url = "http://localhost:8081/v1",
        configuration = FeignConfig.class
)
public interface MainFeignClient {
// user의 닉네임
    @RequestMapping(method = RequestMethod.GET, value = "/accommodation/{accommodationId}")
    AccommodationResponse getAccommodationDto(@PathVariable Long accommodationId);

    @RequestMapping(method = RequestMethod.GET, value = "/chat/room/list/chat")
    List<ChatRoomListGetResponse> getChatRoomList(@RequestHeader("Authorization") String accessToken);

}
