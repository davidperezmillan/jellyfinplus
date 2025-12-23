package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SeriesWithEpisodesTest {

    @Test
    void shouldCreateSeriesWithEpisodesWithAllFields() {


        String id = "12345";
        String name = "Breaking Bad";
        String status = "Continuing";
        double communityRating = 8.5;
        String premiereDate = "2008-01-20";

        Episode episode = new Episode("124", "series1", "Episode 1", "1", 1, 2, true, false);
        // Given
        Series series = new Series(id, name, status, communityRating, premiereDate);
        List<Episode> episodes = List.of(
                new Episode("123", "series1", "Episode 1", "1", 1, 1, true, false)
        );

        // When
        SeriesWithEpisodes seriesWithEpisodes = new SeriesWithEpisodes(series, episodes,episode);

        // Then
        assertThat(seriesWithEpisodes.series()).isEqualTo(series);
        assertThat(seriesWithEpisodes.episodes()).isEqualTo(episodes);
    }
}
