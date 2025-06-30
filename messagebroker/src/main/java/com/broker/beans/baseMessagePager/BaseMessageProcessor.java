package com.broker.beans.baseMessagePager;

import org.springframework.beans.factory.annotation.Autowired;

import com.broker.beans.MessageStatusUpdater;
import com.broker.beans.messagePager.MessageProcessor;
import com.broker.beans.messageSerializer.MessageSerializer;

import jakarta.transaction.Transactional;

public abstract class BaseMessageProcessor<M> implements MessageProcessor<M> {

    @Autowired
    private MessageStatusUpdater statusUpdater;

    private MessageSerializer<M> serializer;

    @Override
    public boolean processRawMessage(String message) {
        Long messageId = getMessageId(message);

        statusUpdater.updateMessageProcessingStart(messageId);

        process(serializer.deserializeMessage(message));

        statusUpdater.updateMessageProcessingEnd(messageId);
        return true;
    }

    public void setSerializer(MessageSerializer<M> serializer) {
        this.serializer = serializer;
    }

    @Transactional
    public abstract boolean process(M message);
}
