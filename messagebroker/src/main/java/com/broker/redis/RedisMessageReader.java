package com.broker.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.broker.beans.baseMessagePager.BaseMessageReader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class RedisMessageReader extends BaseMessageReader {

    private String queueName;
    private String server;
    private StringRedisTemplate stringRedisTemplate;
    private String processorName;

    @Override
    public String read() {
        return stringRedisTemplate.opsForList().leftPop(queueName);
    }

}
