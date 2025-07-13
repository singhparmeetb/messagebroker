package com.broker.beans.messageSerializer;

import org.springframework.stereotype.Component;

@Component("DefaultMessageSerializer")
public class DefaultMessageSerializer implements MessageSerializer<String> {

    @Override
    public String deserializeMessage(String message) {
        return message;
    }

    @Override
    public String serializeMessage(String message) {
        return message;
    }

}
