package com.broker.messageProcessor;

import org.springframework.stereotype.Component;

import com.broker.beans.baseMessageBroker.BaseMessageProcessor;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Setter
@Log4j2
@Component("SimpleMessageProcessor")
public class SimpleProcessor extends BaseMessageProcessor<String> {
    @Override
    public boolean process(String message) {
        log.info("Inside Message Processor trying to process message {}", message);
        return true;
    }

}
