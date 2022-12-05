package com.lightspeedhq.counter.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TestConfiguration
public class ExecutorConfiguration {

    @Bean
    public ExecutorService threadPool() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1,
                runnable -> new Thread(runnable, "IntegrationThread-"));
    }
}
