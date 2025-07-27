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

    @Autowired
    protected Notifier notifier;

    private Map<String, MessageProcessor> messageProcessorsByName;

    private Map<String, MessageSerializer> messageSerializersByName;

    private ApplicationContext applicationContext;

    @PostConstruct
    private void init() {

        log.debug("Inside BaseMessageInitator PostConstruct");

        queuesConfig.getProducerConfigs().forEach(x -> {
            populateMessageSerializerMap(x.getMessageSerializer());
        });

        queuesConfig.getReaderConfigs().forEach(x -> {
            populateProcessorMap(x);
            populateMessageSerializerMap(x.getMessageSerializer());
        });

        // notifier.setApplicationName(applicationContext.getApplicationName());

        // instantiateReaderAndSenders();

    }

    // public Integer void getProcessorId();

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
            serializer = getMessageSerializersByName(readerConfig.getMessageSerializer());
        }
        messageProcessor.setSerializer(serializer);
        messageProcessor.setMessageProcessorName(readerConfig.getMessageProcessor());

        messageProcessorsByName.put(readerConfig.getMessageProcessor(), messageProcessor);

    }

    protected MessageProcessor getMessageProcessor(String messageProcessor) {
        if (!messageProcessorsByName.containsKey(messageProcessor)) {
            log.error("No such processor Found {} in MessageInitiator", messageProcessor);
            throw new ExecutionException("Invalid MessageProcessor");
        }
        return messageProcessorsByName.get(messageProcessor);
    }

    protected QueueFilter getQueueFilterByName(String queueFilterName, String filterCriteria) {
        QueueFilter filter = applicationContext.getBean(queueFilterName, QueueFilter.class);
        filter.setFilterCriteria(filterCriteria);
        return filter;
    }

    private void populateMessageSerializerMap(String serializerName) {
        MessageSerializer serializer = null;
        if (serializerName != null && !serializerName.isBlank()) {
            serializer = applicationContext.getBean(serializerName,
                    MessageSerializer.class);
        }

        if (this.messageSerializersByName == null) {
            this.messageSerializersByName = new HashMap<String, MessageSerializer>();
        }

        if (serializer != null && !messageSerializersByName.containsKey(serializerName)) {
            messageSerializersByName.put(serializerName, serializer);
        }
    }

    protected MessageSerializer getMessageSerializersByName(String messageSerializer) {
        if (!messageSerializersByName.containsKey(messageSerializer)) {
            return applicationContext.getBean("DefaultMessageSerializer", MessageSerializer.class);
        } else {
            return messageSerializersByName.get(messageSerializer);
        }
    }

    // public abstract void instantiateReaderAndSenders();

}
