package com.catchroom.chat.message.service;

import com.catchroom.chat.message.dto.AccommodationResponse;
import com.catchroom.chat.message.feign.MainFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class MainFeignService {

    private final MainFeignClient mainFeignClient;

    public AccommodationResponse getAccommodationResponse(Long accommodationId) {
        return mainFeignClient.getAccommodationDto(accommodationId);
    }

}
