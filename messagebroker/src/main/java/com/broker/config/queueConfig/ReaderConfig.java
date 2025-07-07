package com.broker.config.queueConfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReaderConfig {
    private String queue;
    private String server;
    private String messageProcessor;
    private String messageSerializer;
    private Integer numberOfInstances;

    public Integer getNumberOfInstances() {
        if (this.numberOfInstances == null) {
            return 1;
        }
        return numberOfInstances;
    }

}
