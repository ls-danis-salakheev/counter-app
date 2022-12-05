package com.lightspeedhq.counter.service;

import com.lightspeedhq.counter.annotation.Unit;
import com.lightspeedhq.counter.service.impl.CustomCounterService;
import com.lightspeedhq.counter.util.generator.NameGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@Unit
class CustomCounterServiceTest {

    @InjectMocks
    private CustomCounterService customCounterService;

    @Mock
    private NameGenerator<String> customNameGenerator;

    @Mock
    private Map<String, Long> storedMap;

    private static Map<String, Long> testData;

    @BeforeAll
    static void beforeAll() throws IOException {
        testData = new ConcurrentHashMap<>();
        ClassPathResource resource = new ClassPathResource("/data/test.data");
        Files.readAllLines(resource.getFile().toPath())
                .stream()
                .map(input -> input.split(";"))
                .forEach(array -> testData.put(array[0], Long.valueOf(array[1])));
    }

    @Test
    @DisplayName("Create and return new counter")
    void createAndReturn() {
        String newName = "new name";
        Mockito.when(customNameGenerator.generate()).thenReturn(newName);
        Mockito.when(storedMap.put(newName, 0L)).thenReturn(null);

        String result = customCounterService.create();
        assertNotNull(result);
        assertEquals(newName, result);
    }

    @Test
    @DisplayName("Sum not empty counts")
    void sumNotEmptyCounts() {
        Mockito.when(storedMap.values()).thenReturn(testData.values());
        Long sum = customCounterService.sumCounters();

        assertNotNull(sum);
        assertNotEquals(0, sum);
    }

    @Test
    @DisplayName("Sum not empty counts")
    void sumEmptyCounts() {
        Mockito.when(storedMap.values()).thenReturn(new ArrayList<>());
        Long sum = customCounterService.sumCounters();

        assertNotNull(sum);
        assertEquals(0, sum);
    }

    @Test
    @DisplayName("Get names")
    void getNames() {
        Mockito.when(storedMap.keySet()).thenReturn(testData.keySet());

        Set<String> names = customCounterService.getNames();
        assertEquals(testData.keySet().size(), names.size());
    }
}