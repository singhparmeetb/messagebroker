package com.broker.beans.filters;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.broker.exception.ExecutionException;

@Component("MessageTypeFilter")
@Scope("prototype")

public class MessageTypeFilter implements QueueFilter {

    private String filterCrieteria;

    @Override
    public boolean filter(Object messageToFilter) {
        if (!isApplicable(messageToFilter)) {
            return false;
        }
        String castedMessage = (String) messageToFilter;
        String[] tokens = castedMessage.split("~");
        return filterCrieteria.equals(tokens[0]);
    }

    @Override
    public void setFilterCriteria(String filter) {
        if (filter == null || filter.isBlank()) {
            throw new ExecutionException("Invalid Filter Criteria");
        }
        this.filterCrieteria = filter;
    }

    @Override
    public boolean isApplicable(Object messageToFilter) {
        return messageToFilter instanceof String;
    }

}
