package com.catchroom.chat.feign.client;

import static org.junit.jupiter.api.Assertions.*;

import com.catchroom.chat.global.common.SuccessMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootTest(properties = {"env=test", "feign.client.config.default.loggerLevel=FULL"})
class MainFeignClientTest {

    @Autowired
    MainFeignClient mainFeignClient;
    @Test
    void deleteChatRoom() {
        String accessToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoi7ISx7KeA7Jq0IiwicGhvbmVOdW1iZXIiOiIwMTAxMTExMjIyMiIsIm5pY2tOYW1lIjoi7KeA7Jq0IiwiZW1haWwiOiJ0ZXN0MTEyMkBtYWlsLmNvbSIsImlzcyI6ImNhdGNocm9vbSIsImlhdCI6MTcwNTk4MDA4NCwiZXhwIjoxNzA1OTgxODgzfQ.6Kj4Y2HPekK7S2XE5-99h3lmxiwzUmMoDLMrkM7mUaEpEJsxCB8ZVgxtYllocM9b8jRkuhYST3m5m39JFn9DMA";
        String roomId = "2678bef2-5798-45b0-b6e5-466251ecfe09";

        SuccessMessage successMessage = mainFeignClient.deleteChatRoom(
            accessToken, roomId
        );
        assertEquals("SUCCESS", successMessage.Meassage());
    }
}