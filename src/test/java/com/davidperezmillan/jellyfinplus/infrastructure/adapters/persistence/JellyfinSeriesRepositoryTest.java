package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.model.Series;
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
class JellyfinSeriesRepositoryTest {

    @Mock
    private JellyfinApiClient apiClient;

    @InjectMocks
    private JellyfinSeriesRepository repository;

    @Test
    void findAll_shouldReturnAllSeriesFromApiClient() {
        // Given
        List<Series> expectedSeries = List.of(
                new Series("1", "Series 1", "Overview 1", true)
        );
        when(apiClient.getSeries()).thenReturn(expectedSeries);

        // When
        List<Series> result = repository.findAll();

        // Then
        assertThat(result).isEqualTo(expectedSeries);
    }

    @Test
    void findDownloaded_shouldReturnDownloadedSeriesFromApiClient() {
        // Given
        List<Series> expectedDownloaded = List.of(
                new Series("1", "Series 1", "Overview 1", true)
        );
        when(apiClient.getSeries()).thenReturn(expectedDownloaded);

        // When
        List<Series> result = repository.findDownloaded();

        // Then
        assertThat(result).isEqualTo(expectedDownloaded);
    }
}
