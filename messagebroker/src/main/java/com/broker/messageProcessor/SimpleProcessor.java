package com.broker.messageProcessor;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Setter
@Log4j2
public class SimpleProcessor {
    public void process(String message) {
        log.info("Inside Message Processor trying to process message {}", message);
    }
}
