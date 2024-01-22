package com.catchroom.chat.message.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageType {
    ENTER, TALK, QUIT, NEGO_REQ, NEGO_ALLOW, NEGO_DENIED, DELETE
}
