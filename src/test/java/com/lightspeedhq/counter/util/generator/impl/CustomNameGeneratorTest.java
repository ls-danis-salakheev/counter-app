package com.lightspeedhq.counter.util.generator.impl;

import com.lightspeedhq.counter.annotation.Integration;
import com.lightspeedhq.counter.util.generator.NameGenerator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestConstructor;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Integration
@RequiredArgsConstructor
@TestConstructor(autowireMode = ALL)
class CustomNameGeneratorTest {

    private static final String EMPTY = "";

    private final ExecutorService threadPool;
    private final NameGenerator<String> customNameGenerator;

    @Test
    @DisplayName("Generated id name should be not null and not empty")
    void generatedValueNotNullAndNotEmpty() {
        String generatedName = customNameGenerator.generate();

        assertNotNull(generatedName);
        assertNotEquals(generatedName, EMPTY);
    }

    @Test
    @DisplayName("N of calls should be equal to unique elements in the Set")
    void testUniqueEquality(@Value("${test.unique.set.size}") int size,
                            @Value("${test.unique.set.timeout}") int timeout) throws InterruptedException {
        Set<String> generatedSet = ConcurrentHashMap.newKeySet(size);

        IntStream.range(0, size)
                .forEach(iter -> CompletableFuture
                        .supplyAsync(customNameGenerator::generate, threadPool)
                        .thenAccept(generatedSet::add));
        boolean timeoutMark = threadPool.awaitTermination(timeout, TimeUnit.SECONDS);

        assertFalse(timeoutMark);
        assertEquals(size, generatedSet.size());
    }
}