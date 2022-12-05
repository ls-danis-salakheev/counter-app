package com.lightspeedhq.counter.service.impl;

import com.lightspeedhq.counter.exception.CounterProcessingException;
import com.lightspeedhq.counter.service.CounterService;
import com.lightspeedhq.counter.util.generator.NameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * An in-memory implementation for manipulation with counters and their data.
 */
@Service
@RequiredArgsConstructor
public class CustomCounterService implements CounterService {

    public static final String NOT_FOUND_MESSAGE = "Counter with name %s not found";

    private final Map<String, Long> storageMap;
    private final NameGenerator<String> customNameGenerator;

    /**
     * Creates and stores a unique name (id) with initial count,
     * specified by {@link java.util.concurrent.ConcurrentHashMap}
     *
     * @return the created name
     */
    @Override
    public String create() {
        String name = customNameGenerator.generate();
        storageMap.put(name, 0L);
        return name;
    }

    /**
     * Atomically increments the found count by given {@code #name},
     * specified by {@link java.util.concurrent.ConcurrentHashMap}
     *
     * @param name the counter name
     * @return the updated value
     * @throws CounterProcessingException if the counter with {@code name} is not contained in the storage
     */
    @Override
    public Long incrementByName(String name) {
        return storageMap.compute(name, (key, count) -> {
            if (count != null) {
                return count + 1L;
            } else {
                throw new CounterProcessingException(NOT_FOUND_MESSAGE.formatted(name));
            }
        });
    }

    /**
     * Returns the current value of the found counter,
     * specified by {@link java.util.concurrent.ConcurrentHashMap}
     *
     * @param name the counter name
     * @return the current value
     * @throws CounterProcessingException if the counter with {@code name} is not contained in the storage
     */
    @Override
    public Long getByName(String name) {
        return storageMap.computeIfAbsent(name, count -> {
            throw new CounterProcessingException(NOT_FOUND_MESSAGE.formatted(name));
        });
    }

    /**
     * Remove the counter found by {@code name} in the storage,
     * specified by {@link java.util.concurrent.ConcurrentHashMap}
     *
     * @param name the counter name
     * @throws CounterProcessingException if the counter with {@code name} is not contained in the storage
     */
    @Override
    public void removeByName(String name) {
        storageMap.compute(name, (key, count) -> {
            if (count != null) {
                return storageMap.remove(key);
            } else {
                throw new CounterProcessingException(NOT_FOUND_MESSAGE.formatted(name));
            }
        });
    }

    /**
     * Returns the current sum of all counters.
     * The returned value is not an atomic snapshot; invocation in the absence of
     * concurrent updates returns an accurate result,
     * but concurrent updates that occur while
     * the sum is being calculated might not be incorporated.
     *
     * @return the sum
     */
    @Override
    public Long sumCounters() {
        return storageMap.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    /**
     * Returns a set of unique counters names
     *
     * @return the set
     */
    @Override
    public Set<String> getNames() {
        return storageMap.keySet();
    }
}
