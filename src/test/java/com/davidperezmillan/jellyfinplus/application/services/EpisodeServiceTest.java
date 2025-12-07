package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.ports.EpisodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EpisodeServiceTest {

    @Mock
    private EpisodeRepository episodeRepository;

    @InjectMocks
    private EpisodeService episodeService;

    @Test
    void getEpisodesBySeries_shouldReturnEpisodesForSeries() {
        // Given
        String seriesId = "series1";
        List<Episode> expectedEpisodes = List.of(
                new Episode("1", "Episode 1", "Overview 1", seriesId, 1, 1, true, false),
                new Episode("2", "Episode 2", "Overview 2", seriesId, 1, 2, false, true)
        );
        when(episodeRepository.findBySeriesId(seriesId)).thenReturn(expectedEpisodes);

        // When
        List<Episode> result = episodeService.getEpisodesBySeries(seriesId);

        // Then
        assertThat(result).isEqualTo(expectedEpisodes);
    }

    @Test
    void getUnwatchedEpisodesBySeries_shouldReturnOnlyUnwatchedEpisodes() {
        // Given
        String seriesId = "series1";
        List<Episode> allEpisodes = List.of(
                new Episode("1", "Episode 1", "Overview 1", seriesId, 1, 1, true, false),
                new Episode("2", "Episode 2", "Overview 2", seriesId, 1, 2, false, true),
                new Episode("3", "Episode 3", "Overview 3", seriesId, 1, 3, true, false)
        );
        List<Episode> expectedUnwatched = List.of(
                new Episode("1", "Episode 1", "Overview 1", seriesId, 1, 1, true, false),
                new Episode("3", "Episode 3", "Overview 3", seriesId, 1, 3, true, false)
        );
        when(episodeRepository.findBySeriesId(seriesId)).thenReturn(allEpisodes);

        // When
        List<Episode> result = episodeService.getUnwatchedEpisodesBySeries(seriesId);

        // Then
        assertThat(result).isEqualTo(expectedUnwatched);
    }
}
