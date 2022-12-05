package com.lightspeedhq.counter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightspeedhq.counter.annotation.Web;
import com.lightspeedhq.counter.controller.advice.dto.ApiExceptionResponse;
import com.lightspeedhq.counter.exception.CounterProcessingException;
import com.lightspeedhq.counter.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.lightspeedhq.counter.service.impl.CustomCounterService.NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Web
@RequiredArgsConstructor
@TestConstructor(autowireMode = ALL)
class CounterControllerTest {

    private static final String API_URL = "/api/v1/counter";
    private static final String EMPTY = "";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final CounterService customCounterService;

    @Nested
    class ExceptionTests {

        @Test
        @DisplayName("Should throw exception when counter not found")
        void throwExceptionByIncrement() throws Exception {
            String name = "0-1668562519134-577-2";
            when(customCounterService.incrementByName(name)).thenThrow(new CounterProcessingException(NOT_FOUND_MESSAGE));

            String content = mockMvc.perform(patch(API_URL + "/" + name + "/increment"))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(content);
            assertNotEquals(EMPTY, content);
            ApiExceptionResponse response = objectMapper.readValue(content, ApiExceptionResponse.class);
            assertEquals(response.message(), NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("Should throw exception when counter not found")
        void throwExceptionWhenGetByName() throws Exception {
            String name = "0-1668562519134-577-2";
            when(customCounterService.getByName(name)).thenThrow(new CounterProcessingException(NOT_FOUND_MESSAGE));

            String content = mockMvc.perform(get(API_URL + "/" + name))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();

            assertNotNull(content);
            assertNotEquals(EMPTY, content);
            ApiExceptionResponse response = objectMapper.readValue(content, ApiExceptionResponse.class);
            assertEquals(response.message(), NOT_FOUND_MESSAGE);
        }
    }

    @Test
    @DisplayName("Create name")
    void createName() throws Exception {
        String expected = "0-1668562519134-577-8";
        when(customCounterService.create()).thenReturn(expected);

        mockMvc.perform(post(API_URL))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost" + API_URL + "/" + expected));
    }

    @Test
    @DisplayName("Count all values")
    void countValues() throws Exception {
        Long expected = 1000L;
        when(customCounterService.sumCounters()).thenReturn(expected);

        mockMvc.perform(get(API_URL + "/sum"))
                .andExpect(status().isOk())
                .andExpect(content().string(expected.toString()));
    }

    @Test
    @DisplayName("Get all names")
    void getAllNames() throws Exception {
        Set<String> expected = Set.of("0-1668562519134-577-1", "0-1668562519134-577-2", "0-1668562519134-577-3", "0-1668562519134-577-8");
        when(customCounterService.getNames()).thenReturn(expected);

        mockMvc.perform(get(API_URL + "/names"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Should increment by name")
    void incrementByName() throws Exception {
        String name = "0-1668562519134-577-2";
        Long nextCount = 10L;
        when(customCounterService.incrementByName(name)).thenReturn(nextCount);

        mockMvc.perform(patch(API_URL + "/" + name + "/increment"))
                .andExpect(status().isOk())
                .andExpect(content().string(nextCount.toString()));
    }

    @Test
    @DisplayName("Remove by name")
    void removeByName() throws Exception {
        String name = "0-1668562519134-577-2";
        doNothing().when(customCounterService).removeByName(name);

        mockMvc.perform(delete(API_URL + "/" + name))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Get existing counter by name")
    void getValue() throws Exception {
        String name = "0-1668562519134-577-2";
        Long expected = 10L;
        when(customCounterService.getByName(name)).thenReturn(expected);

        mockMvc.perform(get(API_URL + "/" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(expected.toString()));
    }
}