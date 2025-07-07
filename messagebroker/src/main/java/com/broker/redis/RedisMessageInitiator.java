package com.broker.redis;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broker.beans.filters.QueueFilter;
import com.broker.config.queueConfig.ProducerConfig;
import com.broker.config.queueConfig.ReaderConfig;
import com.broker.messagebroker.MessageInitiator;
import com.broker.redis.config.RedisServers;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class RedisMessageInitiator
        extends MessageInitiator {

    @Autowired
    private RedisServers redisServers;

    private List<RedisMessageReader> readers;

    private List<RedisMessageSender> senders;

    @PostConstruct
    private void init() {
        getQueuesConfig().getReaderConfigs().forEach(x -> {
            for (int i = 0; i < x.getNumberOfInstances(); i++) {
                readers.add(instantiateReader(x));
            }
        });

        getQueuesConfig().getProducerConfigs().forEach(x -> {
            senders.add(instantiateSender(x));
        });

        startAllReaders();
    }

    private RedisMessageReader instantiateReader(ReaderConfig readerConfig) {
        RedisMessageReader redisMessageReader = new RedisMessageReader(readerConfig.getQueue(),
                readerConfig.getServer(), redisServers.getStringRedisTemplate(readerConfig.getServer()),
                readerConfig.getMessageProcessor(),
                false);
        redisMessageReader.setMessageProcessor(getMessageProcessor(readerConfig.getMessageProcessor()));
        return redisMessageReader;
    }

    private void startAllReaders() {
        readers.stream().forEach(reader -> {
            reader.start();
        });
    }

    private RedisMessageSender instantiateSender(ProducerConfig producerConfig) {
        RedisMessageSender redisMessageSender = new RedisMessageSender(producerConfig.getQueue(),
                redisServers.getStringRedisTemplate(producerConfig.getServer()), producerConfig.getServer());
        redisMessageSender.setMessageSerializer(getMessageSerializer(producerConfig.getMessageSerializer()));

        QueueFilter queueFilter = getQueueFilterBy(producerConfig.getFilter());
        redisMessageSender.setQueueFilter(queueFilter);

        return redisMessageSender;
    }

    @PreDestroy
    private void shutDownAllReaders() {
        readers.stream().forEach(reader -> {
            reader.shutdownReader();
        });
    }
}
