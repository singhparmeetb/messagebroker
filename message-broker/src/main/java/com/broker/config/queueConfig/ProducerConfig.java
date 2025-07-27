package com.broker.config.queueConfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProducerConfig {
    private String queue;
    private String server;
    private String messageSerializer;
    private String filter;
    private String filterCriteria;

}
