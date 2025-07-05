package com.broker.beans.filters;

public interface QueueFilter {

    boolean filter(Object messageToFilter);

    void setFilterCriteria(String filter);

    boolean isApplicable(Object messageToFilter);

}
