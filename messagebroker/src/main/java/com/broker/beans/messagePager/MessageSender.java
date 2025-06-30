package com.broker.beans.messagePager;

public interface MessageSender<M> {
    void send(String message);
}
