package com.catchroom.chat.feign.controller;

import com.catchroom.chat.feign.service.MainFeignService;
import com.catchroom.chat.feign.dto.AccommodationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class MainFeignController {
    private final MainFeignService mainFeignService;
    @GetMapping("/accommodation/{accommodationId}")
    public AccommodationResponse getAccommodation(@PathVariable Long accommodationId) {
        return mainFeignService.getAccommodationResponse(accommodationId);
    }
}
