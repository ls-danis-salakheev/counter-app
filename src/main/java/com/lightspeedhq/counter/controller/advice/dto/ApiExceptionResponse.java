package com.lightspeedhq.counter.controller.advice.dto;

import java.time.Instant;

/**
 * External exception handling for the view layer
 *
 * @param message   the exception message
 * @param path      the servlet path
 * @param timestamp the current timestamp
 */
public record ApiExceptionResponse(String message,
                                   String path,
                                   Instant timestamp) {
}
