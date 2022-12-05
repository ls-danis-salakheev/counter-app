package com.lightspeedhq.counter.controller.advice;

import com.lightspeedhq.counter.controller.CounterController;
import com.lightspeedhq.counter.controller.advice.dto.ApiExceptionResponse;
import com.lightspeedhq.counter.exception.CounterProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

/**
 * A controller advice for catching/handling exceptions on the view layer
 */
@ControllerAdvice(basePackageClasses = {CounterController.class})
public class ApiExceptionControllerAdvice {

    /**
     * Handles not found exception
     *
     * @param exception the caught exception
     * @param request   the current request
     * @return the response
     */
    @ResponseBody
    @ExceptionHandler({CounterProcessingException.class})
    public ResponseEntity<ApiExceptionResponse> handleNotFoundException(CounterProcessingException exception,
                                                                        HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(new ApiExceptionResponse(exception.getMessage(), request.getServletPath(), Instant.now()));
    }
}
