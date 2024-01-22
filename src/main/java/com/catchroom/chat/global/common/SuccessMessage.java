package com.catchroom.chat.global.common;

public record SuccessMessage(
        String Meassage
) {
    public static SuccessMessage createSuccessMessage(String message) {
        return new SuccessMessage(message);
    }
}
