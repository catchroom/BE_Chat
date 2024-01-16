package com.catchroom.chat.message.feign;

import com.catchroom.chat.global.config.FeignConfig;
import com.catchroom.chat.message.dto.AccommodationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "mainFeign", url = "https://catchroom.xyz/v1",
        configuration = FeignConfig.class
)
public interface MainFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/accommodation/{accommodationId}")
    AccommodationResponse getAccommodationDto(@PathVariable Long accommodationId);
}
