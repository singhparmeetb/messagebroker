package com.broker.beans.messageBroker;

public interface MessageProcessor<M> {
    boolean processRawMessage(String rawMessage);

    Long getMessageId(String rawMessage);
}
