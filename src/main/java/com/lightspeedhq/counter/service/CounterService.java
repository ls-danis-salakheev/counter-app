package com.lightspeedhq.counter.service;

import java.util.Set;

public interface CounterService {

    String create();

    Long incrementByName(String name);

    Long getValueByName(String name);

    void removeByName(String name);

    Long sumCounters();

    Set<String> getNames();

}
