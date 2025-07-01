package com.broker.beans.messageSerializer;

public interface MessageSerializer<O> {
    public O deserializeMessage(String rawMessage);

    public String serializeMessage(O message);
}
