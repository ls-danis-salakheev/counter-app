package com.lightspeedhq.counter.util.generator.impl;

import com.lightspeedhq.counter.util.generator.NameGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Default UUID generator based on {@link java.util.UUID}
 */
@Component
public class UUIDGenerator implements NameGenerator<UUID> {

    /**
     * Creates a unique UUID using {@link UUID#randomUUID()}
     * @return the UUID-typed id
     */
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
