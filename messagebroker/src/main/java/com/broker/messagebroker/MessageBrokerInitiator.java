package com.broker.messagebroker;

import org.springframework.beans.factory.annotation.Autowired;

import com.broker.config.queueConfig.QueuesConfig;

public class MessageBrokerInitiator {

    @Autowired
    private QueuesConfig queuesConfig;
}
