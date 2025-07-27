package com.broker.beans;

import com.broker.exception.ExecutionException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@AllArgsConstructor
public class GenericMessage {

    private Long messageId;
    private String message;
    private Integer retryCount;

    public Integer getRetryCount() {
        if (this.retryCount == null) {
            return 0;
        } else {
            return this.retryCount;
        }
    }

    public Long getMessageId() {
        if (this.messageId == null) {
            return -1L;
        } else {
            return this.messageId;
        }
    }

    public String getMessageToSend() {
        return getMessageId() + "~" + getMessage() + "~" + getRetryCount();
    }

    public GenericMessage(String message) {
        String[] tokens = message.split("~");
        if (!(tokens.length == 2 || tokens.length == 3)) {
            log.debug("Invalid message found {}", message);
            throw new ExecutionException("Invalid message found " + message);
        }
        Long messageId = Long.parseLong(tokens[0]);
        Integer retryCount = tokens.length == 3 ? Integer.parseInt(tokens[2]) : 1;

        this.messageId = messageId;
        this.message = tokens[1];
        this.retryCount = retryCount;

    }

}
