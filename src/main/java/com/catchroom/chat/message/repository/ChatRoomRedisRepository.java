package com.catchroom.chat.message.repository;

import com.catchroom.chat.chatroom.dto.ChatRoomListGetResponse;
import com.catchroom.chat.message.dto.ChatMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** redis 와 관련된 메소드들 */


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomRedisRepository {

    private static final String CHAT_ROOM = "CHAT_ROOM_LAST_MSG"; //채팅방 마지막 메시지 저장

    private static final String CHAT_ROOM_LIST = "_CHAT_ROOM_LIST";

    private static final String CHAT_ROOM_KEY = "_CHAT_ROOM_RESPONSE_LIST";


    private final ObjectMapper objectMapper;


    private final RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> listValueOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoomListGetResponse> opsHashChatRoom;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatMessageDto> opsHashLastChatMessage;


    private String getChatRoomKey(Long userId) {
        return userId + CHAT_ROOM_KEY;
    }

    public boolean existChatRoomList(Long userId) {
        return redisTemplate.hasKey(getChatRoomKey(userId));
    }

    public void initChatRoomList(Long userId, List<ChatRoomListGetResponse> list) {
        if (redisTemplate.hasKey(getChatRoomKey(userId))) {
            redisTemplate.delete(getChatRoomKey(userId));
        }

        opsHashChatRoom = redisTemplate.opsForHash();
        for (ChatRoomListGetResponse chatRoomListGetRes : list) {
            setChatRoom(userId, chatRoomListGetRes.getChatRoomNumber(), chatRoomListGetRes);
        }

    }

    public void setChatRoom(Long userId, String roomId, ChatRoomListGetResponse response) {
        opsHashChatRoom.put(getChatRoomKey(userId), roomId, response);
    }

    public boolean existChatRoom(Long userId, String roomId) {
        return opsHashChatRoom.hasKey(getChatRoomKey(userId), roomId);
    }

    public void deleteChatRoom(Long userId, String roomId) {
        opsHashChatRoom.delete(getChatRoomKey(userId), roomId);
    }

    public ChatRoomListGetResponse getChatRoom(Long userId, String roomId) {
        return opsHashChatRoom.get(getChatRoomKey(userId), roomId);
    }

    public List<ChatRoomListGetResponse> getChatRoomList(Long userId) {
        return opsHashChatRoom.values(getChatRoomKey(userId));
    }


    public void setLastChatMessage(String roomId, ChatMessageDto chatMessageDto) {
        opsHashLastChatMessage.put(CHAT_ROOM, roomId, chatMessageDto);
    }

    public ChatMessageDto getLastMessage(String roomId) {
        return opsHashLastChatMessage.get(CHAT_ROOM, roomId);
    }


    public void setChatRoomList(Long userId, List<ChatRoomListGetResponse> list) {
        String key = userId + CHAT_ROOM_LIST;
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);

        listValueOperations = redisTemplate.opsForValue();
        listValueOperations.set(key, list);
    }



    /******************** 밑에 코드 사용 안하지만 일단 냅둠 ***************************/

    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> userInfoOps;

    // 세션 id와 유저 정보 가져오기.
    public String getUserInfoBySessionId(String sessionId) {
        return userInfoOps.get(sessionId);
    }

    // 퇴장 시 세션 id 값 삭제
    public void deleteUserInfo(String sessionId) {
        userInfoOps.getAndDelete(sessionId);
    }

    // 유저가 입장한 채팅방 ID와 유저 세션 ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방 ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }


}

