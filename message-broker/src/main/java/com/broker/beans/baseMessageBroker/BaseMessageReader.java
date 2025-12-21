package com.broker.beans.baseMessageBroker;

import com.broker.beans.messageBroker.MessageProcessor;
import com.broker.beans.messageBroker.MessageReader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class BaseMessageReader implements MessageReader, Runnable {

    protected MessageProcessor messageProcessor;

    public void readAndProcess() {

        String message = read();
        if (message == null || message.isBlank()) {
            return;
        }
        try {
            messageProcessor.processMessage(message);
        } catch (Exception e) {
            log.error("Unable to Process Message {} due to exception {}", message, e);
        }

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

}
