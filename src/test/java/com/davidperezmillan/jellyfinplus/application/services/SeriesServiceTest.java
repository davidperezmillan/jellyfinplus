package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.ports.SeriesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

    @Mock
    private SeriesRepository seriesRepository;

    @InjectMocks
    private SeriesService seriesService;

    @Test
    void getAllSeries_shouldReturnAllSeries() {
        // Given
        List<Series> expectedSeries = List.of(
                new Series("1", "Series 1", "Continuing", 9.0, "2008-01-20"),
                new Series("2", "Series 2", "Ended", 8.5, "2019-05-10")
        );
        when(seriesRepository.findAll()).thenReturn(expectedSeries);

        // When
        List<Series> result = seriesService.getAllSeries();

        // Then
        assertThat(result).isEqualTo(expectedSeries);
    }
}
