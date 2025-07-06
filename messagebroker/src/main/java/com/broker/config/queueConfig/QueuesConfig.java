package com.broker.config.queueConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "queues")
public class QueuesConfig {
    private List<ReaderConfig> readers;
    private List<ProducerConfig> producers;

    public Stream<ReaderConfig> getReaderConfigs() {
        if (this.readers == null) {
            return new ArrayList<ReaderConfig>().stream();
        }
        return readers.stream();
    }

    public Stream<ProducerConfig> getProducerConfigs() {
        if (this.producers == null) {
            return new ArrayList<ProducerConfig>().stream();
        }
        return producers.stream();
    }
}
