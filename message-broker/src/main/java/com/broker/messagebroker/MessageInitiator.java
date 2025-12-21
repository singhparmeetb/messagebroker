package com.broker.messagebroker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.broker.beans.baseMessageBroker.BaseMessageProcessor;
import com.broker.beans.baseMessageBroker.BaseMessageSender;
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

        if (!messageProcessorsByName.containsKey(readerConfig.getMessageProcessor())) {
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

    }

    protected MessageProcessor getMessageProcessor(String messageProcessor) {
        if (!messageProcessorsByName.containsKey(messageProcessor)) {
            log.error("No such processor Found {} in MessageInitiator", messageProcessor);
            throw new ExecutionException("Invalid MessageProcessor " + messageProcessor + " found");
        }
        return messageProcessorsByName.get(messageProcessor);
    }

    protected QueueFilter getQueueFilterByName(String queueFilterName, String filterCriteria) {
        QueueFilter filter = applicationContext.getBean(queueFilterName, QueueFilter.class);
        filter.setFilterCriteria(filterCriteria);
        return filter;
    }

    private void populateMessageSerializerMap(String serializerName) {

        if (messageSerializersByName == null) {
            messageSerializersByName = new HashMap<String, MessageSerializer>();
        }
        if (serializerName == null || serializerName.isBlank()) {
            log.info("Invalid Serializer {} Found", serializerName);
            return;
        } else if (messageSerializersByName.containsKey(serializerName)) {
            log.info("Serializer {} already created", serializerName);
            return;
        }

        MessageSerializer serializer = applicationContext.getBean(serializerName,
                MessageSerializer.class);
        messageSerializersByName.put(serializerName, serializer);

    }

    protected MessageSerializer getMessageSerializersByName(String messageSerializer) {
        if (!messageSerializersByName.containsKey(messageSerializer)) {
            log.warn("No Serializer Found with name {}");
            throw new ExecutionException("No Serializer Of Name " + messageSerializer + " found");
        } else {
            return messageSerializersByName.get(messageSerializer);
        }
    }

    protected void addProducer(BaseMessageSender sender) {
        notifier.producers.add(sender);
    }

    // public abstract void instantiateReaderAndSenders();

}
