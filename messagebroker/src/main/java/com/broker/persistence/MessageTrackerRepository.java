package com.broker.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.broker.beans.MessageStatuses;
import com.broker.beans.MessageTracker;

public interface MessageTrackerRepository extends JpaRepository<MessageTracker, Long> {

    @Modifying
    @Query(value = "UPDATE MessageTracker e SET e.status=?2, e.processorName=?3 WHERE e.id=?1")
    Long updateMessageProcessing(Long messageId, MessageStatuses status, String processorName);
}
