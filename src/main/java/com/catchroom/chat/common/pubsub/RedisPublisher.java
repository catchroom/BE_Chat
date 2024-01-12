package com.catchroom.chat.common.pubsub;

import com.catchroom.chat.message.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    public void publish(ChatMessageDto message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
