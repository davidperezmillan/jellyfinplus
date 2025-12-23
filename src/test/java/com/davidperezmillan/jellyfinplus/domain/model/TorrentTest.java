package com.davidperezmillan.jellyfinplus.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TorrentTest {

    @Test
    void shouldCreateTorrentWithAllFields() {
        // Given
        int id = 1;
        String name = "Test Torrent";
        String status = "downloading";
        double percentDone = 50.0;
        long totalSize = 1000L;
        long rateDownload = 100L;
        long rateUpload = 50L;

        // When
        Torrent torrent = new Torrent(id, name, status, percentDone, totalSize, rateDownload, rateUpload);

        // Then
        assertThat(torrent.id()).isEqualTo(id);
        assertThat(torrent.name()).isEqualTo(name);
        assertThat(torrent.status()).isEqualTo(status);
        assertThat(torrent.percentDone()).isEqualTo(percentDone);
        assertThat(torrent.totalSize()).isEqualTo(totalSize);
        assertThat(torrent.rateDownload()).isEqualTo(rateDownload);
        assertThat(torrent.rateUpload()).isEqualTo(rateUpload);
    }
}
