package com.broker.beans;

import java.time.LocalDateTime;
import java.util.Optional;

import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.broker.persistence.MessageTrackerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageStatusUpdater {

    @Autowired
    private MessageTrackerRepository messageTrackerRepository;

    @Transactional
    public void updateMessageProcessingEnd(Long messageId) {
        log.debug("Updating Processing End {}", messageId);
        Optional<MessageTracker> message = messageTrackerRepository.findById(messageId);
        if (message.isPresent()) {
            message.get().setStatus(MessageStatuses.PROCESSED);
            message.get().setProcessingEndTime(LocalDateTime.now());
            messageTrackerRepository.save(message.get());
        } else {
            log.warn("Invalid Message Id {} found in {}", messageId, Thread.currentThread().getName());
            throw new ExecutionException("Invalid Message Id " + messageId + "found");
        }
    }

    @Transactional
    public void updateMessageProcessingStart(Long messageId, String processorName) {
        log.debug("Updating Processing Start {}", messageId);
        Optional<MessageTracker> message = messageTrackerRepository.findById(messageId);
        if (message.isPresent()) {
            message.get().setProcessorName(processorName);
            message.get().setStatus(MessageStatuses.PROCESSING);
            message.get().setProcessingStartTime(LocalDateTime.now());
            messageTrackerRepository.save(message.get());
        } else {
            log.warn("Invalid Message Id {} found in {}", messageId, Thread.currentThread().getName());
            throw new ExecutionException("Invalid Message Id " + messageId + "found");
        }
    }

    @Transactional
    public Long prepareToSendMessage(String channel, String fromSystem, String server, String message) {
        MessageTracker messageToSend = populateMessageTracker(channel, fromSystem, server, message);
        return messageTrackerRepository.save(messageToSend).getId();
    }

    @Transactional
    public void updateMessageSent(GenericMessage genericMessage) {
        log.debug("Updating Processing Start {}", genericMessage);
        Optional<MessageTracker> message = messageTrackerRepository.findById(genericMessage.getMessageId());
        if (message.isPresent()) {
            message.get().setStatus(MessageStatuses.SENT);
            message.get().setLastSentTime(LocalDateTime.now());
            message.get().setMessage(genericMessage.getMessageToSend());
            messageTrackerRepository.save(message.get());
        } else {
            log.warn("Invalid Message Id {} found in {}", genericMessage.getMessageId(),
                    Thread.currentThread().getName());
            throw new ExecutionException("Invalid Message Id " + genericMessage.getMessageId() + "found");
        }
    }

    private MessageTracker populateMessageTracker(String channel, String fromSystem, String server, String message) {
        MessageTracker messageTracker = new MessageTracker();
        messageTracker.setFromSystem(fromSystem);
        messageTracker.setChannel(channel);
        messageTracker.setServer(server);
        messageTracker.setStatus(MessageStatuses.PENDING);
        messageTracker.setRetryCount(0);
        messageTracker.setLastSentTime(LocalDateTime.now());
        messageTracker.setMessage(message);

        return messageTracker;
    }
}
