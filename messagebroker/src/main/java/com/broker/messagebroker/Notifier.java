package com.broker.messagebroker;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.broker.beans.MessageStatusUpdater;
import com.broker.beans.baseMessageBroker.BaseMessageSender;
import com.broker.exception.ExecutionException;

import lombok.Setter;

@Setter
@Component
public class Notifier {

    private List<BaseMessageSender> producers;

    private String applicationName;

    @Autowired
    private MessageStatusUpdater messageStatusUpdater;

    public void sendMessage(Object message) {
        Optional<BaseMessageSender> producerToSend = producers.stream()
                .filter(producer -> producer.getQueueFilter().filter(message))
                .findFirst();
        if (!producerToSend.isPresent()) {
            throw new ExecutionException("No Queue Found To Send Message On");
        } else {
            // M castedMessage = (M) message;
            String messageToSend = producerToSend.get().getMessageSerializer().serializeMessage(message);

            Long messageId = messageStatusUpdater.prepareToSendMessage(producerToSend.get().getQueueName(),
                    applicationName,
                    messageToSend);

            producerToSend.get().send(messageToSend);

            messageStatusUpdater.updateMessageSent(messageId);

        }

    }

}
