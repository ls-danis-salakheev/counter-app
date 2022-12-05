package com.lightspeedhq.counter.util.generator;

import java.io.Serializable;

/**
 * Interface for implementation an unique names (ids)
 *
 * @param <T> type
 */
public interface NameGenerator<T extends Serializable> {
    T generate();
}
