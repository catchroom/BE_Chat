package com.catchroom.chat.feign.service;

import com.catchroom.chat.feign.client.MainFeignClient;
import com.catchroom.chat.feign.dto.AccommodationResponse;
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
  