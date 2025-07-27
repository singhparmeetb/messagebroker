package com.broker.beans.baseMessageBroker;

import org.springframework.beans.factory.annotation.Autowired;

import com.broker.beans.GenericMessage;
import com.broker.beans.MessageStatusUpdater;
import com.broker.beans.messageBroker.MessageProcessor;
import com.broker.beans.messageSerializer.MessageSerializer;

import jakarta.transaction.Transactional;

public abstract class BaseMessageProcessor<M> implements MessageProcessor<M> {

    @Autowired
    private MessageStatusUpdater statusUpdater;

    private MessageSerializer<M> serializer;

    private String messageProcessorName;

    @Override
    public boolean processMessage(String rawMessage) {

        GenericMessage genericMessage = new GenericMessage(rawMessage);

        statusUpdater.updateMessageProcessingStart(genericMessage.getMessageId(), messageProcessorName);

        process(serializer.deserializeMessage(genericMessage.getMessage()));

        statusUpdater.updateMessageProcessingEnd(genericMessage.getMessageId());

        return true;
    }

    public void setSerializer(MessageSerializer<M> serializer) {
        this.serializer = serializer;
    }

    public void setMessageProcessorName(String messageProcessor) {
        this.messageProcessorName = messageProcessor;
    }

    @Transactional
    public abstract boolean process(M message);
}
