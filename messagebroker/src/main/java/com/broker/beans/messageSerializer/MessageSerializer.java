package com.broker.beans.messageSerializer;

public interface MessageSerializer<O> {
    public O deserializeMessage(String rawMessage);

    public String serialiceMessage(O message);
}
