package com.catchroom.chat.message.controller;

import com.catchroom.chat.message.dto.AccommodationResponse;
import com.catchroom.chat.message.service.MainFeignService;
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
