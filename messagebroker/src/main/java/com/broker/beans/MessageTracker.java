package com.broker.beans;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "messagetracker")
@Getter
@Setter
@ToString
public class MessageTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "from_system", nullable = false)
    private String fromSystem;

    @Column(name = "queue_name", nullable = false)
    private String queueName;

    @Column(name = "processor_name")
    private String processorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatuses status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "server", nullable = false)
    private String server;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
