package com.catchroom.chat.global.exception;

import com.catchroom.chat.global.common.SuccessMessage;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessMessage> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(
                SuccessMessage.createSuccessMessage("ERROR")
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<SuccessMessage> handleFeignException(FeignException e) {
        log.error("존재하지 않는 엑세스 토큰입니다. ");
        return ResponseEntity.badRequest().body(
                SuccessMessage.createSuccessMessage("ACCESS TOKEN ERROR")
        );
    }
}
