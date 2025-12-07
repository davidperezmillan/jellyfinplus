package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.domain.model.Episode;
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

@WebMvcTest(EpisodeController.class)
class EpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EpisodeService episodeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEpisodesBySeries_shouldReturnEpisodesList() throws Exception {
        // Given
        String seriesId = "series1";
        List<Episode> episodes = List.of(
                new Episode("1", "Episode 1", "Overview 1", seriesId, 1, 1, true, false)
        );
        when(episodeService.getEpisodesBySeries(seriesId)).thenReturn(episodes);

        // When & Then
        mockMvc.perform(get("/api/episodes/series/{seriesId}", seriesId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(episodes)));
    }

    @Test
    void getUnwatchedEpisodesBySeries_shouldReturnUnwatchedEpisodesList() throws Exception {
        // Given
        String seriesId = "series1";
        List<Episode> unwatchedEpisodes = List.of(
                new Episode("1", "Episode 1", "Overview 1", seriesId, 1, 1, true, false),
                new Episode("3", "Episode 3", "Overview 3", seriesId, 1, 3, true, false)
        );
        when(episodeService.getUnwatchedEpisodesBySeries(seriesId)).thenReturn(unwatchedEpisodes);

        // When & Then
        mockMvc.perform(get("/api/episodes/series/{seriesId}/unwatched", seriesId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(unwatchedEpisodes)));
    }
}
