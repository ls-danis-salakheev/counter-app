package com.lightspeedhq.counter.exception;

/**
 * Thrown to notice any processing error during working with counters.
 */
public class CounterProcessingException extends RuntimeException {

    public CounterProcessingException(String message) {
        super(message);
    }
}
