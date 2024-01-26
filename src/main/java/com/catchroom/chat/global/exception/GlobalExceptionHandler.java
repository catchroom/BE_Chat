package com.catchroom.chat.global.exception;

import com.catchroom.chat.global.common.SuccessMessage;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessMessage> handleException(Exception e) {
        return ResponseEntity.badRequest().body(
                SuccessMessage.createSuccessMessage("ERROR")
        );
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<SuccessMessage> handleFeignException(FeignException e) {
        return ResponseEntity.badRequest().body(
                SuccessMessage.createSuccessMessage("TOKEN ERROR")
        );
    }
}
