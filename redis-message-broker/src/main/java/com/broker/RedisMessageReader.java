package com.broker;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.broker.beans.baseMessageBroker.BaseMessageReader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@AllArgsConstructor
@Log4j2
public class RedisMessageReader extends BaseMessageReader implements Runnable {

    private String queueName;
    private String server;
    private StringRedisTemplate stringRedisTemplate;
    private String processorName;
    private String readerName;

    private volatile boolean shutdown = false;

    @Override
    public String read() {
        return stringRedisTemplate.opsForList().leftPop(queueName);
    }

    @Override
    public void run() {
        log.debug("Starting to Poll Queue{} from {}", queueName, Thread.currentThread().getName());
        while (!shutdown) {
            readAndProcess();
        }
        log.debug("Shutting Down {}", Thread.currentThread().getName());
    }

    public void shutdownReader() {
        this.shutdown = true;
    }

    public void start() {
        Thread.currentThread().start();
    }

}
