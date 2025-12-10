package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.model.SeriesWithEpisodes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
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

    @MockitoBean
    private EpisodeService episodeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllSeries_shouldReturnSeriesList() throws Exception {
        // Given
        List<Series> series = List.of(
                new Series("1", "Series 1", "Continuing", 9.0, "2008-01-20")
        );
        when(seriesService.getAllSeries()).thenReturn(series);

        // When & Then
        mockMvc.perform(get("/api/series"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(series)));
    }

    @Test
    void getSeriesWithEpisodes_noUnwatchedParam_returnsAllEpisodes() throws Exception {
        // Given
        Series s = new Series("1", "Series 1", "Continuing", 9.0, "2008-01-20");
        List<Series> series = List.of(s);
        when(seriesService.getAllSeries()).thenReturn(series);

        List<Episode> episodes = new ArrayList<>();
        episodes.add(new Episode("e1", "Episode 1", "ov", "1", 1, 1, true, true));
        episodes.add(new Episode("e2", "Episode 2", "ov2", "1", 1, 2, true, false));
        when(episodeService.getEpisodesBySeries("1")).thenReturn(episodes);
        when(episodeService.getNextEpisode("1")).thenReturn(null);

        List<SeriesWithEpisodes> expected = List.of(new SeriesWithEpisodes(s, episodes, null));

        // When & Then
        mockMvc.perform(get("/api/series/with-episodes"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void getSeriesWithEpisodes_unwatchedTrue_returnsOnlyUnwatched() throws Exception {
        // Given
        Series s = new Series("1", "Series 1", "Continuing", 9.0, "2008-01-20");
        List<Series> series = List.of(s);
        when(seriesService.getAllSeries()).thenReturn(series);

        List<Episode> unwatched = new ArrayList<>();
        unwatched.add(new Episode("e2", "Episode 2", "ov2", "1", 1, 2, true, false));
        when(episodeService.getUnwatchedEpisodesBySeries("1")).thenReturn(unwatched);
        when(episodeService.getNextEpisode("1")).thenReturn(null);

        List<SeriesWithEpisodes> expected = List.of(new SeriesWithEpisodes(s, unwatched, null));

        // When & Then
        mockMvc.perform(get("/api/series/with-episodes").param("unwatched", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    void getSeriesWithEpisodesByTitle_shouldReturnSeriesWithEpisodes() throws Exception {
        // Given
        Series s = new Series("1", "Series 1", "Continuing", 9.0, "2008-01-20");
        List<Series> series = List.of(s);
        when(seriesService.getAllSeries()).thenReturn(series);

        List<Episode> episodes = new ArrayList<>();
        episodes.add(new Episode("e1", "Episode 1", "ov", "1", 1, 1, true, false));
        when(episodeService.getEpisodesBySeries("1")).thenReturn(episodes);
        when(episodeService.getNextEpisode("1")).thenReturn(null);

        List<SeriesWithEpisodes> expected = List.of(new SeriesWithEpisodes(s, episodes, null));

        // When & Then
        mockMvc.perform(get("/api/series/with-episodes/search").param("title", "Series 1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }
}
