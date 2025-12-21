package com.broker;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.broker.beans.baseMessageBroker.BaseMessageReader;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class RedisMessageReader extends BaseMessageReader {

    private String queueName;
    private String server;
    private StringRedisTemplate stringRedisTemplate;
    private String processorName;
    private String readerName;

    private Thread thread;

    private volatile boolean shutdown = false;

    public RedisMessageReader(String queueName, String server, StringRedisTemplate stringRedisTemplate,
            String processorName, String readerName) {
        this.queueName = queueName;
        this.server = server;
        this.stringRedisTemplate = stringRedisTemplate;
        this.processorName = processorName;
        this.readerName = readerName;
    }

    @Override
    public String read() {
        return stringRedisTemplate.opsForList().leftPop(queueName, Duration.ofSeconds(10));
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
        this.thread = new Thread(this, readerName);
        this.thread.start();
    }

}
