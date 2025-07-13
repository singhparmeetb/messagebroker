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

    @Column(name = "channel", nullable = false)
    private String channel;

    @Column(name = "processor_name")
    private String processorName;

    @Column(name = "message", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MessageStatuses status;

    @Column(name = "processing_start_time")
    private LocalDateTime processingStartTime;

    @Column(name = "processing_end_time")
    private LocalDateTime processingEndTime;

    @Column(name = "last_sent_time")
    private LocalDateTime lastSentTime;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "server", nullable = false)
    private String server;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
