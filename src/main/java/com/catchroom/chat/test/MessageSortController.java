package com.catchroom.chat.test;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.dto.MessageSubDto;
import com.catchroom.chat.message.type.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/v1/chat/list/sort/test")
@RequiredArgsConstructor
public class MessageSortController {
    private final ChatRoomService chatRoomService;
    @GetMapping()
    public MessageSubDto getChatRoomListAndMessage(
        @RequestHeader("Authorization") String accessToken
    ) {
        ChatMessageDto chatMessageDto = new ChatMessageDto();
        chatMessageDto.setType(MessageType.ENTER);
        chatMessageDto.setRoomId("test-room-id-kkkk");
        chatMessageDto.setUserId(4L);
        chatMessageDto.setMessage("test 메세지 보냄");
        chatMessageDto.setTime(String.valueOf(LocalDateTime.now()));
        chatMessageDto.setUserCount(1L);
        chatMessageDto.setNegoPrice(-1);

        List<ChatRoomListGetResponse> list = chatRoomService.getChatRoomList(accessToken);

        Comparator<ChatRoomListGetResponse> comparator = new Comparator<ChatRoomListGetResponse>() {
            @Override
            public int compare(ChatRoomListGetResponse o1, ChatRoomListGetResponse o2) {
                if (o1.getLastChatmessageDto() != null && o2.getLastChatmessageDto() != null) {
                    return LocalDateTime.parse(o2.getLastChatmessageDto().getTime()).withNano(0).compareTo(LocalDateTime.parse(o1.getLastChatmessageDto().getTime()).withNano(0));
                } else {
                    return 0;
                }
            }

        };
        Collections.sort(list,comparator);
        MessageSubDto messageSubDto = MessageSubDto.builder()
            .chatMessageDto(chatMessageDto)
            .list(list)
            .build();


        return messageSubDto;
    }
}
