package com.broker.messagebroker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.broker.beans.MessageStatusUpdater;
import com.broker.beans.baseMessageBroker.BaseMessageSender;
import com.broker.exception.ExecutionException;

import lombok.Setter;

@Setter
@Component
public class Notifier {

    private List<BaseMessageSender> producers = new ArrayList<BaseMessageSender>();

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private MessageStatusUpdater messageStatusUpdater;

    public void sendMessage(Object message, Object filter) {
        Optional<BaseMessageSender> producerToSend = producers.stream()
                .filter(producer -> producer.getQueueFilter().isApplicable(message))
                .filter(producer -> producer.getQueueFilter().filter(
                        filter))
                .findFirst();
        if (!producerToSend.isPresent()) {
            throw new ExecutionException("No Queue Found To Send Message On");
        } else {
            // M castedMessage = (M) message;
            String messageToSend = producerToSend.get().getMessageSerializer().serializeMessage(message);

            Long messageId = messageStatusUpdater.prepareToSendMessage(producerToSend.get().getQueueName(),
                    applicationName, producerToSend.get().getServer(),
                    messageToSend);

            producerToSend.get().send(messageToSend);

            messageStatusUpdater.updateMessageSent(messageId);

        }

    }

    public void addProducer(BaseMessageSender sender) {
        this.producers.add(sender);
    }

}
