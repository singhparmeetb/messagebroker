package com.broker.beans;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.broker.persistence.MessageTrackerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageStatusUpdater {

    @Autowired
    private MessageTrackerRepository messageTrackerRepository;

    @Transactional
    public void updateMessageProcessingEnd(Long messageId) {
        log.debug("Updating Processing End {}", messageId);
        Optional<MessageTracker> message = messageTrackerRepository.findById(messageId);
        if (message.isPresent()) {
            message.get().setStatus(MessageStatuses.PROCESSED);
            message.get().setEndTime(LocalDateTime.now());
            messageTrackerRepository.save(message.get());
        } else {
            log.warn("Invalid Message Id {} found in {}", messageId, Thread.currentThread().getName());
            throw new ExecutionException("Invalid Message Id" + " messageId " + "found");
        }
    }

    @Transactional
    public void updateMessageProcessingStart(Long messageId) {
        log.debug("Updating Processing Start {}", messageId);
        Optional<MessageTracker> message = messageTrackerRepository.findById(messageId);
        if (message.isPresent()) {
            message.get().setProcessorName(null);
            message.get().setStatus(MessageStatuses.PROCESSING);
            message.get().setStartTime(LocalDateTime.now());
            messageTrackerRepository.save(message.get());
        } else {
            log.warn("Invalid Message Id {} found in {}", messageId, Thread.currentThread().getName());
            throw new ExecutionException("Invalid Message Id" + " messageId " + "found");
        }
    }

    @Transactional
    public Long prepareToSendMessage(String channel, String fromSystem, String message) {
        MessageTracker messageToSend = populateMessageTracker(channel, fromSystem, message);
        messageTrackerRepository.save(messageToSend);
        return messageToSend.getId();
    }

    @Transactional
    public void updateMessageSent(Long messageId) {
        log.debug("Updating Processing Start {}", messageId);
        Optional<MessageTracker> message = messageTrackerRepository.findById(messageId);
        if (message.isPresent()) {
            message.get().setStatus(MessageStatuses.SENT);
            message.get().setLastSentTime(LocalDateTime.now());
            messageTrackerRepository.save(message.get());
        } else {
            log.warn("Invalid Message Id {} found in {}", messageId, Thread.currentThread().getName());
            throw new ExecutionException("Invalid Message Id" + " messageId " + "found");
        }
    }

    private MessageTracker populateMessageTracker(String channel, String fromSystem, String message) {
        MessageTracker messageTracker = new MessageTracker();
        messageTracker.setFromSystem(fromSystem);
        messageTracker.setChannel(channel);
        messageTracker.setStatus(MessageStatuses.PENDING);
        messageTracker.setRetryCount(0);
        messageTracker.setLastSentTime(LocalDateTime.now());
        messageTracker.setMessage(message);

        return messageTracker;
    }
}
