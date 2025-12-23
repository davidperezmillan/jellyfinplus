package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.response;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransmissionRestResponseTest {

    @Test
    void shouldCreateTransmissionRestResponseWithAllFields() {
        // Given
        int count = 2;
        List<Torrent> torrents = List.of(
                new Torrent(1, "Torrent 1", "downloading", 50.0, 1000L, 100L, 50L),
                new Torrent(2, "Torrent 2", "seeding", 100.0, 2000L, 0L, 100L)
        );

        // When
        TransmissionRestResponse response = new TransmissionRestResponse(count, torrents);

        // Then
        assertThat(response.getCount()).isEqualTo(count);
        assertThat(response.getTorrents()).isEqualTo(torrents);
    }
}
