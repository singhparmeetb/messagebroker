package com.broker.beans.filters;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.broker.exception.ExecutionException;

import lombok.extern.log4j.Log4j2;

@Component("MessageSizeFilter")
@Scope("prototype")
@Log4j2
public class MessageSizeFilter implements QueueFilter {

    private Long messageSizeCriteria;
    private String operator;

    @Override
    public boolean filter(Object inputFilter) {

        log.debug("Inside Filter {}", inputFilter);

        Long messageSize = (Long) inputFilter;
        if (operator.equals("GE")) {
            return messageSize >= messageSizeCriteria;
        } else if (operator.equals("GT")) {
            return messageSize > messageSizeCriteria;
        } else if (operator.equals("LE")) {
            return messageSize <= messageSizeCriteria;
        } else if (operator.equals("LT")) {
            return messageSize < messageSizeCriteria;
        } else if (operator.equals("EQ")) {
            return messageSize == messageSizeCriteria;
        }
        return false;
    }

    @Override
    public void setFilterCriteria(String filter) {
        log.debug("Setting filterCriteria {}", filter);

        String[] tokens = filter.split(" ");

        if (!validate(tokens[0])) {
            log.warn("Invalid Filter found {}", filter);
            throw new ExecutionException("Invalid Operator Found");
        }

        operator = tokens[0];
        messageSizeCriteria = Long.parseLong(tokens[1]);
    }

    private boolean validate(String inputOperator) {
        return inputOperator.equals("GE") || inputOperator.equals("GT") || inputOperator.equals("LE")
                || inputOperator.equals("LT")
                || (inputOperator.equals("EQ"));

    }

    @Override
    public boolean isApplicable(Object message) {
        boolean result = message instanceof String;
        log.debug("Inside isApplicable {} and result {}", message, result);
        return result;
    }

}
