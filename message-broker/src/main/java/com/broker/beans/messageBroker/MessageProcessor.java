package com.broker.beans.messageBroker;

public interface MessageProcessor<M> {

    boolean processMessage(String rawMessage);

}
