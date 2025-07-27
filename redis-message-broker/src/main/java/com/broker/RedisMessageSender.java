package com.broker;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.broker.beans.baseMessageBroker.BaseMessageSender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class RedisMessageSender extends BaseMessageSender<String> {

    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void send(String message) {
        log.debug("Sending message to queue {} :: {}", queueName, message);
        stringRedisTemplate.opsForList().rightPush(queueName, message);
    }

    public RedisMessageSender(String queue, StringRedisTemplate stringRedisTemplate, String server) {
        this.queueName = queue;
        this.stringRedisTemplate = stringRedisTemplate;
        this.server = server;
    }

    // @Override
    // public void send(M message, String queueToSend) {
    // stringRedisTemplate.opsForList().rightPush(queueToSend,
    // messageSerializer.serializeMessage(message));
    // }

}
