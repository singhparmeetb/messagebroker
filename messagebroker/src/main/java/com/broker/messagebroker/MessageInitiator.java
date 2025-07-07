package com.broker.messagebroker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.broker.beans.baseMessageBroker.BaseMessageProcessor;
import com.broker.beans.filters.QueueFilter;
import com.broker.beans.messageBroker.MessageProcessor;
import com.broker.beans.messageSerializer.MessageSerializer;
import com.broker.config.queueConfig.ProducerConfig;
import com.broker.config.queueConfig.QueuesConfig;
import com.broker.config.queueConfig.ReaderConfig;
import com.broker.exception.ExecutionException;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Component
@Log4j2
public abstract class MessageInitiator implements ApplicationContextAware {

    @Autowired
    private QueuesConfig queuesConfig;

    private Map<String, QueueFilter> filterByNames;

    private Map<String, MessageProcessor> messageProcessorsByName;

    private Map<String, MessageSerializer> messageSerializersByName;

    private ApplicationContext applicationContext;

    @PostConstruct
    private void init() {
        queuesConfig.getProducerConfigs().forEach(x -> {
            populateFilterMap(x);
            populateMessageSerializerMap(x);
        });

        queuesConfig.getReaderConfigs().forEach(x -> {
            populateProcessorMap(x);
            populateMessageSerializerMap(x);
        });

    }

    // public Integer void getProcessorId();

    private void populateFilterMap(ProducerConfig producerConfig) {
        if (filterByNames == null) {
            filterByNames = new HashMap<>();
        } else if (producerConfig.getFilter() == null || producerConfig.getFilter().isBlank()
                || producerConfig.getFilterCriteria() == null || producerConfig.getFilterCriteria().isBlank()) {
            return;
        }

        QueueFilter filter = applicationContext.getBean(producerConfig.getFilter(), QueueFilter.class);
        filter.setFilterCriteria(producerConfig.getFilterCriteria());
        filterByNames.put(producerConfig.getFilter(), filter);
    }

    private void populateProcessorMap(ReaderConfig readerConfig) {

        if (messageProcessorsByName == null) {
            messageProcessorsByName = new HashMap<>();
        }

        BaseMessageProcessor messageProcessor = (BaseMessageProcessor) applicationContext
                .getBean(readerConfig.getMessageProcessor());

        MessageSerializer serializer;
        if (readerConfig.getMessageSerializer() == null || readerConfig.getMessageSerializer().isBlank()) {
            serializer = applicationContext.getBean("DefaultMessageSerializer",
                    MessageSerializer.class);
        } else {
            serializer = applicationContext.getBean(readerConfig.getMessageSerializer(),
                    MessageSerializer.class);
        }
        messageProcessor.setSerializer(serializer);

        messageProcessorsByName.put(readerConfig.getMessageProcessor(), messageProcessor);

    }

    protected MessageProcessor getMessageProcessor(String messageProcessor) {
        if (!messageProcessorsByName.containsKey(messageProcessor)) {
            log.error("No such processor Found {} in MessageInitiator", messageProcessor);
            throw new ExecutionException("Invalid MessageProcessor");
        }
        return messageProcessorsByName.get(messageProcessor);
    }

    protected QueueFilter getQueueFilterBy(String filter) {
        return filterByNames.get(filter);
    }

    private void populateMessageSerializerMap(ReaderConfig readerConfig) {
        MessageSerializer serializer = null;
        if (readerConfig.getMessageSerializer() != null || !readerConfig.getMessageSerializer().isBlank()) {
            serializer = applicationContext.getBean(readerConfig.getMessageSerializer(),
                    MessageSerializer.class);
        }

        if (this.messageSerializersByName == null) {
            this.messageSerializersByName = new HashMap<String, MessageSerializer>();
        }

        if (serializer != null && !messageSerializersByName.containsKey(readerConfig.getMessageSerializer())) {
            messageSerializersByName.put(readerConfig.getMessageSerializer(), serializer);
        }
    }

    private void populateMessageSerializerMap(ProducerConfig producerConfig) {
        MessageSerializer serializer = null;
        if (producerConfig.getMessageSerializer() != null || !producerConfig.getMessageSerializer().isBlank()) {
            serializer = applicationContext.getBean(producerConfig.getMessageSerializer(),
                    MessageSerializer.class);
        }

        if (this.messageSerializersByName == null) {
            this.messageSerializersByName = new HashMap<String, MessageSerializer>();
        }

        if (serializer != null && !messageSerializersByName.containsKey(producerConfig.getMessageSerializer())) {
            messageSerializersByName.put(producerConfig.getMessageSerializer(), serializer);
        }
    }

    protected MessageSerializer getMessageSerializer(String messageSerializer) {
        if (!messageSerializersByName.containsKey(messageSerializer)) {
            return applicationContext.getBean("DefaultMessageSerializer", MessageSerializer.class);
        } else {
            return messageSerializersByName.get(messageSerializer);
        }
    }

}
