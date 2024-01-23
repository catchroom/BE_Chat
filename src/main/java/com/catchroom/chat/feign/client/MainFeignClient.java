package com.catchroom.chat.feign.client;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.global.common.SuccessMessage;
import com.catchroom.chat.global.config.FeignConfig;
import com.catchroom.chat.feign.dto.AccommodationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 채팅 서버 입장 (시큐리티 X)
 * userId로 user 정보를 가져오는 상황
 * 비로그인 사용자도 접근할 수 있음.
 *
 *
 */
@FeignClient(
        name = "mainFeign", url = "https://catchroom.xyz/v1",
        configuration = FeignConfig.class
)
public interface MainFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/accommodation/{accommodationId}")
    AccommodationResponse getAccommodationDto(@PathVariable Long accommodationId);

    @RequestMapping(method = RequestMethod.GET, value = "/chat/room/list/chat")
    List<ChatRoomListGetResponse> getChatRoomList(@RequestHeader("Authorization") String accessToken);

    @RequestMapping(method = RequestMethod.DELETE, value = "/chat/room")
    SuccessMessage deleteChatRoom(
            @RequestHeader("Authorization") String accessToken,
            @RequestParam(name = "roomId") String roomId
    );

}
