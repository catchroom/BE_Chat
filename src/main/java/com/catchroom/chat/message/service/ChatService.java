package com.catchroom.chat.message.service;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.chatroom.service.ChatRoomService;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.global.pubsub.RedisPublisher;
import com.catchroom.chat.message.dto.MessageSubDto;
import com.catchroom.chat.message.repository.ChatRoomRedisRepository;
import com.catchroom.chat.message.type.MessageType;
import com.catchroom.chat.message.type.UserIdentity;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomService chatRoomService;

    /**
     * destination 정보에서 roomId 추출
     */
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1)
            return destination.substring(lastIndex + 1);
        else
            return "";
    }

    /**
     * 채팅방에 메시지 발송
     */
    public void sendChatMessage(ChatMessageDto chatMessage, String accessToken) {
        chatRoomRedisRepository.setLastChatMessage(chatMessage.getRoomId(), chatMessage);

        Long userId = chatMessage.getUserId();
        Long partnerId;

        Long before = System.currentTimeMillis();

        // 1. 채팅방이 삭제되는 것이라면 delete 를 해준다.
        if (chatMessage.getType().equals(MessageType.DELETE)) {
            chatRoomService.deleteChatRoom(accessToken, chatMessage.getRoomId(), userId);
        }

        ChatRoomListGetResponse newChatRoomListResponse = null;
        if (!chatRoomRedisRepository.existChatRoom(userId, chatMessage.getRoomId())) {
            newChatRoomListResponse = chatRoomService.getChatRoomInfo(accessToken, chatMessage.getRoomId());
        } else {
            newChatRoomListResponse = chatRoomRedisRepository.getChatRoom(userId, chatMessage.getRoomId());
        }

        partnerId = getPartnerId(chatMessage, newChatRoomListResponse);

        // 2. 채팅방 리스트에 새로운 채팅방 정보가 없다면, 넣어준다. 마지막 메시지도 같이 담는다. 상대방 레디스에도 업데이트 해준다.
        setNewChatRoomInfo(chatMessage, newChatRoomListResponse);

        // 3. 마지막 메시지들이 담긴 채팅방 리스트들을 가져온다. // 4. 파트너 채팅방 리스트도 가져온다. (파트너는 userId 로만)
        List<ChatRoomListGetResponse> chatRoomListGetResponseList = chatRoomService.getChatRoomList(userId, accessToken);
        List<ChatRoomListGetResponse> partnerChatRoomGetResponseList = chatRoomService.getChatRoomListByUserId(partnerId);


        // 5. 마지막 메세지 기준으로 정렬 채팅방 리스트 정렬
        chatRoomListGetResponseList = chatRoomService.sortChatRoomListLatest(chatRoomListGetResponseList);
        partnerChatRoomGetResponseList = chatRoomService.sortChatRoomListLatest(partnerChatRoomGetResponseList);


        MessageSubDto messageSubDto = MessageSubDto.builder()
            .userId(userId)
            .partnerId(partnerId)
            .chatMessageDto(chatMessage)
            .list(chatRoomListGetResponseList)
            .partnerList(partnerChatRoomGetResponseList)
            .build();

        Long after = System.currentTimeMillis() - before;
        log.info("message Time : {}", after);

        redisPublisher.publish(messageSubDto);
    }

    private Long getPartnerId(ChatMessageDto chatMessageDto, ChatRoomListGetResponse my) {
        Long userId = chatMessageDto.getUserId();
        Long partnerId;
        if (my.getBuyerId() == userId) {
            partnerId = my.getSellerId();
        } else {
            partnerId = my.getBuyerId();
        }
        return partnerId;
    }


    /**
     * redis 에 채팅방 정보가 없는 경우 새로 저장.
     * @param chatMessage
     */
    private void setNewChatRoomInfo(ChatMessageDto chatMessage, ChatRoomListGetResponse newChatRoomListResponse) {

        newChatRoomListResponse.updateChatMessageDto(chatMessage);


        /** 상대방 채팅 리스트와 내 리스트 둘다 채팅방을 저장한다. */

        if (newChatRoomListResponse.getLoginUserIdentity().equals(UserIdentity.SELLER)) {
            if (!chatMessage.getType().equals(MessageType.DELETE)) {
                chatRoomRedisRepository.setChatRoom(newChatRoomListResponse.getSellerId(),
                        chatMessage.getRoomId(), newChatRoomListResponse);
            }
            newChatRoomListResponse.changePartnerInfo(); //닉네임 체인지
            chatRoomRedisRepository.setChatRoom(newChatRoomListResponse.getBuyerId(), chatMessage.getRoomId(), newChatRoomListResponse);

        } else if (newChatRoomListResponse.getLoginUserIdentity().equals(UserIdentity.BUYER)){
            if (!chatMessage.getType().equals(MessageType.DELETE)) {
                chatRoomRedisRepository.setChatRoom(newChatRoomListResponse.getBuyerId(),
                        chatMessage.getRoomId(), newChatRoomListResponse);
            }

            newChatRoomListResponse.changePartnerInfo(); //닉네임 체인지
            chatRoomRedisRepository.setChatRoom(newChatRoomListResponse.getSellerId(), chatMessage.getRoomId(), newChatRoomListResponse);
        }

        //다시 원상태로 복귀
        newChatRoomListResponse.changePartnerInfo();

    }






}
