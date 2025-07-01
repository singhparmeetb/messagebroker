package com.broker.beans.messageBroker;

public interface MessageSender<M> {
    void send(M message);

    void send(M message, String queueName);
}
