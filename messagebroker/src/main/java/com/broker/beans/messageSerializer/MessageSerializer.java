package com.broker.beans.messageSerializer;

public interface MessageSerializer<M> {
    public M deserializeMessage(String rawMessage);

    public String serializeMessage(M message);
}
