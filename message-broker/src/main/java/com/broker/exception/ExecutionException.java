package com.broker.exception;

public class ExecutionException extends RuntimeException {
    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException() {
        super();
    }

}
