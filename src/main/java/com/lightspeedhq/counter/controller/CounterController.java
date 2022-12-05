package com.lightspeedhq.counter.controller;

import com.lightspeedhq.counter.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

/**
 * An interface for manipulation counter using REST
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/counter")
public class CounterController {

    private final CounterService customCounterService;

    /**
     * Generates and returns a unique name
     *
     * @param uriComponentsBuilder the uri builder
     * @return the unique name
     */
    @PostMapping
    public ResponseEntity<String> createCounter(UriComponentsBuilder uriComponentsBuilder) {
        String name = customCounterService.create();
        return ResponseEntity.created(uriComponentsBuilder.path("/api/v1/counter/{name}")
                        .buildAndExpand(name).toUri())
                .body(name);
    }

    /**
     * Atomically increments and returns updated value
     *
     * @param name the counter name
     * @return the updated value
     */
    @PatchMapping("/{name}/increment")
    public Long incrementByName(@PathVariable String name) {
        return customCounterService.incrementByName(name);
    }

    /**
     * Returns counter by the name
     *
     * @param name the counter name
     * @return the sum total
     */
    @GetMapping("/{name}")
    public Long getCounter(@PathVariable String name) {
        return customCounterService.getByName(name);
    }

    /**
     * Deletes a counter by the name
     *
     * @param name the counter name
     * @return the empty content
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Object> removeByName(@PathVariable String name) {
        customCounterService.removeByName(name);
        return ResponseEntity.noContent().build();
    }

    /**
     * Summarizes all counters
     *
     * @return the sum total
     */
    @GetMapping("/sum")
    public Long sumAllValues() {
        return customCounterService.sumCounters();
    }

    /**
     * Returns the unique counter name
     *
     * @return the set
     */
    @GetMapping("/names")
    public Set<String> getNames() {
        return customCounterService.getNames();
    }

}
