package com.catchroom.chat.feign.controller;

import com.catchroom.chat.feign.service.MainFeignService;
import com.catchroom.chat.feign.dto.AccommodationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
@CrossOrigin(origins = {"https://localhost:3000","https://dev.dhlbrqe2v28e4.amplifyapp.com"}, allowedHeaders = "*")
public class MainFeignController {
    private final MainFeignService mainFeignService;
    @GetMapping("/accommodation/{accommodationId}")
    public AccommodationResponse getAccommodation(@PathVariable Long accommodationId) {
        return mainFeignService.getAccommodationResponse(accommodationId);
    }
}
