package com.broker.beans.messageBroker;

public interface MessageSender<M> {

    void send(String message);

}
