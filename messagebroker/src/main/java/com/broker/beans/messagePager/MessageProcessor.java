package com.broker.beans.messagePager;

public interface MessageProcessor<M> {
    boolean processRawMessage(String rawMessage);

    Long getMessageId(String rawMessage);
}
