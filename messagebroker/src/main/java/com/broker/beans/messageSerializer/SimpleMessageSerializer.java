package com.broker.beans.messageSerializer;

public class SimpleMessageSerializer implements MessageSerializer<String> {

    @Override
    public String deserializeMessage(String message) {
        return message;
    }

    @Override
    public String serialiceMessage(String message) {
        return message;
    }

}
