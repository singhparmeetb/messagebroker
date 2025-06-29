package com.broker.beans;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "server", nullable = false)
    private String server;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
