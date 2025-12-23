package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EpisodeTest {

    @Test
    void shouldCreateEpisodeWithAllFields() {
        // Given
        String id = "episode1";
        String seriesId = "series1";
        String name = "Test Episode";
        int seasonNumber = 1;
        int episodeNumber = 1;
        String overview = "A test episode";
        boolean isDownloaded = true;
        boolean isPlayed = false;

        // When
        Episode episode = new Episode(id, name, overview, seriesId, seasonNumber, episodeNumber, isDownloaded, isPlayed);

        // Then
        assertThat(episode.id()).isEqualTo(id);
        assertThat(episode.seriesId()).isEqualTo(seriesId);
        assertThat(episode.name()).isEqualTo(name);
        assertThat(episode.seasonNumber()).isEqualTo(seasonNumber);
        assertThat(episode.episodeNumber()).isEqualTo(episodeNumber);
        assertThat(episode.overview()).isEqualTo(overview);
        assertThat(episode.isDownloaded()).isEqualTo(isDownloaded);
        assertThat(episode.isPlayed()).isEqualTo(isPlayed);
    }
}
