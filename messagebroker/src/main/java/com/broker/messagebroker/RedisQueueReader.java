package com.broker.messagebroker;

import com.broker.messageProcessor.SimpleProcessor;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@ToString
public class RedisQueueReader implements Runnable {

    private String queueName;
    private StringRedisTemplate stringRedisTemplate;
    private volatile boolean shutdown;

    private SimpleProcessor processor;

    public RedisQueueReader(String queueName, StringRedisTemplate stringRedisTemplate,
            SimpleProcessor processor, String threadName) {

        this.queueName = queueName;
        this.processor = processor;
        this.shutdown = false;
        this.stringRedisTemplate = stringRedisTemplate;
        Thread.currentThread().setName(threadName);
    }

    @Override
    public void run() {
        while (!shutdown) {
            String message = stringRedisTemplate.opsForList().leftPop(queueName);
            if (message != null) {
                log.info("Message Found ");
                processor.process(message);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Shutting down Reader " + Thread.currentThread().getName());
    }

    public void shutdownReader() {
        this.shutdown = true;
    }

}
