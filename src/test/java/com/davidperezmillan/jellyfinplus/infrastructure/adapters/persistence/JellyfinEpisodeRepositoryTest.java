package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.external.JellyfinApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JellyfinEpisodeRepositoryTest {

    @Mock
    private JellyfinApiClient apiClient;

    @InjectMocks
    private JellyfinEpisodeRepository repository;

    @Test
    void findBySeriesId_shouldReturnEpisodesFromApiClient() {
        // Given
        String seriesId = "series1";
        List<Episode> expectedEpisodes = List.of(
                new Episode("1", "Episode 1", "Overview 1", seriesId, 1, 1, true)
        );
        when(apiClient.getEpisodes(seriesId)).thenReturn(expectedEpisodes);

        // When
        List<Episode> result = repository.findBySeriesId(seriesId);

        // Then
        assertThat(result).isEqualTo(expectedEpisodes);
    }

    @Test
    void findDownloaded_shouldReturnDownloadedEpisodesFromApiClient() {
        // Given
        List<Episode> expectedDownloaded = List.of(
                new Episode("1", "Episode 1", "Overview 1", "series1", 1, 1, true)
        );
        when(apiClient.getAllEpisodes()).thenReturn(expectedDownloaded);

        // When
        List<Episode> result = repository.findDownloaded();

        // Then
        assertThat(result).isEqualTo(expectedDownloaded);
    }
}
