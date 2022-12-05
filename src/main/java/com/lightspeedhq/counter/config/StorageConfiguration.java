package com.lightspeedhq.counter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A configuration bean for in-memory application storage.
 */
@Configuration
public class StorageConfiguration {

    @Bean
    public Map<String, Long> storageMap() {
        return new ConcurrentHashMap<>();
    }

}
