package com.broker.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.broker.beans.messageBroker.MessageSender;
import com.broker.beans.messageSerializer.MessageSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class RedisMessageSender<M> implements MessageSender<M> {

    private String queueName;
    private StringRedisTemplate stringRedisTemplate;
    private String server;
    private MessageSerializer<M> messageSerializer;

    @Override
    public void send(M message) {
        stringRedisTemplate.opsForList().rightPush(queueName, messageSerializer.serializeMessage(message));
    }

    @Override
    public void send(M message, String queueToSend) {
        stringRedisTemplate.opsForList().rightPush(queueToSend, messageSerializer.serializeMessage(message));
    }

}
