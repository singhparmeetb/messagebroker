package com.broker.beans.baseMessageBroker;

import com.broker.beans.filters.QueueFilter;
import com.broker.beans.messageBroker.MessageSender;
import com.broker.beans.messageSerializer.MessageSerializer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseMessageSender<M> implements MessageSender<M> {

    private QueueFilter queueFilter;
    private MessageSerializer<M> messageSerializer;
    private String channel;

}
