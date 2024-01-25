package com.catchroom.chat.global.util;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatRoomUtil {
    public static void sortChatRoomListLatest (
            List<ChatRoomListGetResponse> chatRoomListGetResponseList
    ) {

        Comparator<ChatRoomListGetResponse> comparator = (o1, o2) -> {
            if (o1.getLastChatmessageDto() != null && o2.getLastChatmessageDto() != null) {
                return LocalDateTime.parse(o2.getLastChatmessageDto().getTime()).withNano(0)
                        .compareTo(
                                LocalDateTime.parse(o1.getLastChatmessageDto().getTime()).withNano(0)
                        );
            } else {
                return 0;
            }
        };

        Collections.sort(chatRoomListGetResponseList,comparator);
    }
}
