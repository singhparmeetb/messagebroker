package com.broker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broker.beans.MessageTracker;

public interface MessageTrackerRepository extends JpaRepository<MessageTracker, Long> {

}
