package com.broker.config.queueConfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProducerConfig {
    private String queueName;
    private String serverName;
    private String serializerName;
    private String filterCriteria;

}
