package com.catchroom.chat.message.repository;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.dto.ChatRoom;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/** redis 와 관련된 메소드들 */

@RequiredArgsConstructor
@Service
public class ChatRoomRepository {

    private static final String CHAT_ROOM = "CHAT_ROOM_LAST_MSG"; //채팅방
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO";
    // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    // HashOperations<String, String, ChatRoom> : Redis의 해시 데이터 구조를 다룸.
    // String 타입의 key, String 타입의 필드, chatRoom 객체의 값으로 구성된 해시를 다룬다.
    @Resource(name = "redisTemplate") //redisTemplate bean 주입.
    private HashOperations<String, String, ChatMessageDto> opsHashLastChatMessage;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> userInfoOps;

    public void setLastChatMessage(String roomId, ChatMessageDto chatMessageDto) {
        opsHashLastChatMessage.put(CHAT_ROOM, roomId, chatMessageDto);
    }

    public ChatMessageDto getLastMessage(String roomId) {
        return opsHashLastChatMessage.get(CHAT_ROOM, roomId);
    }


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

