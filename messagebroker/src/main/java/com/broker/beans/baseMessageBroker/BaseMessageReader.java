package com.broker.beans.baseMessageBroker;

import com.broker.beans.messageBroker.MessageProcessor;
import com.broker.beans.messageBroker.MessageReader;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseMessageReader implements MessageReader {

    @Setter
    private MessageProcessor messageProcessor;

    public void readAndProcess() {

        String message = read();
        try {
            messageProcessor.processRawMessage(message);
        } catch (Exception e) {
            log.error("Unable to Process Message {} due to exception {}", message, e);
        }

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

}
