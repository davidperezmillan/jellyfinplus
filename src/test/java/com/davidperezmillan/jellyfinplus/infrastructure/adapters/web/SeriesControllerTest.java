package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeriesController.class)
class SeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SeriesService seriesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllSeries_shouldReturnSeriesList() throws Exception {
        // Given
        List<Series> series = List.of(
                new Series("1", "Series 1", "Overview 1", true)
        );
        when(seriesService.getAllSeries()).thenReturn(series);

        // When & Then
        mockMvc.perform(get("/api/series"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(series)));
    }
}
