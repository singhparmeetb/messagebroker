package com.broker.demo_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.broker.messagebroker.Notifier;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class Controller {

    @Autowired
    private Notifier notifier;

    @PostMapping("/pushData")
    public void pushAndList(@RequestParam String value) {
        String[] temp = value.split("~");
        log.debug("Inside Pushing Data to Redis");
        notifier.sendMessage(temp[0], temp[1]);
    }

}
