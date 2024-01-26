package com.catchroom.chat.global.exception;

import com.catchroom.chat.global.common.SuccessMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SuccessMessage> handleBaseException(Exception e) {
        return ResponseEntity.badRequest().body(
                SuccessMessage.createSuccessMessage("ERROR")
        );
    }
}
