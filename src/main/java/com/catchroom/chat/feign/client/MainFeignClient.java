package com.catchroom.chat.feign.client;

import com.catchroom.chat.global.config.FeignConfig;
import com.catchroom.chat.message.dto.AccommodationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
// user의 닉네임
    @RequestMapping(method = RequestMethod.GET, value = "/accommodation/{accommodationId}")
    AccommodationResponse getAccommodationDto(@PathVariable Long accommodationId);



}
