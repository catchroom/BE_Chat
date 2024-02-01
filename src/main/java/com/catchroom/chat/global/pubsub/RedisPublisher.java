package com.catchroom.chat.global.pubsub;

import com.catchroom.chat.message.dto.ChatMessageDto;
import com.catchroom.chat.message.dto.MessageSubDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {
    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    public void publish(MessageSubDto message) {
        log.info("RedisPublisher publishing .. {}", message.getChatMessageDto().getMessage());
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
