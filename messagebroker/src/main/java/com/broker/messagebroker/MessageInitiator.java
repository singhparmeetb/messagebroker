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

    private ApplicationContext applicationContext;

    // @PostConstruct
    // private void init() {
    // String processorName;

    // String applicationName=
    // }

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
                .getBean(readerConfig.getMessageProcessorName());

        MessageSerializer serializer;
        if (readerConfig.getSerializerName() == null || readerConfig.getSerializerName().isBlank()) {
            serializer = applicationContext.getBean("DefaultMessageSerializer",
                    MessageSerializer.class);
        } else {
            serializer = applicationContext.getBean(readerConfig.getSerializerName(),
                    MessageSerializer.class);
        }
        messageProcessor.setSerializer(serializer);

        messageProcessorsByName.put(readerConfig.getMessageProcessorName(), messageProcessor);

    }

    protected MessageProcessor getMessageProcessor(String messageProcessorName) {
        if (!messageProcessorsByName.containsKey(messageProcessorName)) {
            log.error("No such processor Found {} in MessageInitiator", messageProcessorName);
            throw new ExecutionException("Invalid MessageProcessor");
        }
        return messageProcessorsByName.get(messageProcessorName);
    }

}
