package com.broker.beans.baseMessagePager;

import com.broker.beans.messagePager.MessageProcessor;
import com.broker.beans.messagePager.MessageReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseMessageReader implements MessageReader {

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
