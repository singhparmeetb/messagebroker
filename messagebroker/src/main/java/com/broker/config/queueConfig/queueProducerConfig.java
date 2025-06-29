package com.broker.config.queueConfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class queueProducerConfig {
    private String queueName;
    private String serverName;

}
