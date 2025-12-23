package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SeriesTest {

    @Test
    void shouldCreateSeriesWithAllFields() {
        // Given
        String id = "series1";
        String name = "Test Series";
        String status = "status";
        double communityRating = 9.5;
        String premiereDate = "2024-01-01";


        // When
        Series series = new Series(id, name, status, communityRating, premiereDate);

        // Then
        assertThat(series.id()).isEqualTo(id);
        assertThat(series.name()).isEqualTo(name);
        assertThat(series.status()).isEqualTo(status);
        assertThat(series.communityRating()).isEqualTo(communityRating);
        assertThat(series.premiereDate()).isEqualTo(premiereDate);


    }
}
