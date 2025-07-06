package com.broker.redis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broker.config.queueConfig.ReaderConfig;
import com.broker.messagebroker.MessageInitiator;
import com.broker.redis.config.RedisServers;

@Component
public class RedisMessageInitiator
        extends MessageInitiator {

    @Autowired
    private RedisServers redisServers;

    private List<RedisMessageReader> readers;

    private void init() {
        getQueuesConfig().getReaderConfigs().forEach(x -> {
            instantiateReaders(x);
        });

        getQueuesConfig().getProducerConfigs().forEach(null);

        startAllReaders();
    }

    private void instantiateReaders(ReaderConfig readerConfig) {
        RedisMessageReader redisMessageReader = new RedisMessageReader(readerConfig.getQueueName(),
                readerConfig.getServerName(), redisServers.getStringRedisTemplate(readerConfig.getServerName()),
                readerConfig.getMessageProcessorName(),
                false);
        redisMessageReader.setMessageProcessor(getMessageProcessor(readerConfig.getMessageProcessorName()));
        readers.add(redisMessageReader);
    }

    private void startAllReaders() {
        readers.stream().forEach(reader -> {
            reader.start();
        });
    }
}
