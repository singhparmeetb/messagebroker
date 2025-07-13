package com.broker.redis;

import java.util.ArrayList;
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
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class RedisMessageInitiator
        extends MessageInitiator {

    @Autowired
    private RedisServers redisServers;

    private List<RedisMessageReader> readers = new ArrayList<RedisMessageReader>();

    // private List<RedisMessageSender> senders = new
    // ArrayList<RedisMessageSender>();

    @PostConstruct
    private void init() {
        log.debug("Inside RedisMessageInitator PostConstruct");

        getQueuesConfig().getReaderConfigs().forEach(x -> {
            log.debug("InstantiatingReader");
            for (int i = 0; i < x.getNumberOfInstances(); i++) {
                readers.add(instantiateReader(x, i));
            }
        });

        getQueuesConfig().getProducerConfigs().forEach(x -> {
            log.debug("Instantiating Producers");
            notifier.addProducer(instantiateSender(x));
        });

        startAllReaders();
    }

    private RedisMessageReader instantiateReader(ReaderConfig readerConfig, int instanceNumber) {
        RedisMessageReader redisMessageReader = new RedisMessageReader(readerConfig.getQueue(),
                readerConfig.getServer(), redisServers.getStringRedisTemplate(readerConfig.getServer()),
                readerConfig.getMessageProcessor(),
                readerConfig.getQueue() + "-" + instanceNumber, false);
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

        redisMessageSender.setMessageSerializer(getMessageSerializersByName(producerConfig.getMessageSerializer()));

        QueueFilter queueFilter = getQueueFilterByName(producerConfig.getFilter(), producerConfig.getFilterCriteria());
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
