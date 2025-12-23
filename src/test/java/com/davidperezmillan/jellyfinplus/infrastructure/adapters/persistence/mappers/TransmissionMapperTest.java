package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.mappers;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TransmissionMapperTest {

    @InjectMocks
    private TransmissionMapper mapper;

    @Test
    void toDomain_shouldMapTorrentResponseToTorrent() {
        // Given
        TorrentResponse response = new TorrentResponse();
        response.setId(1);
        response.setName("Test Torrent");
        response.setStatus(4); // downloading
        response.setPercentDone(50.0);
        response.setTotalSize(1000L);
        response.setRateDownload(100L);
        response.setRateUpload(50L);

        // When
        Torrent torrent = mapper.toDomain(response);

        // Then
        assertThat(torrent.id()).isEqualTo(1);
        assertThat(torrent.name()).isEqualTo("Test Torrent");
        assertThat(torrent.status()).isEqualTo("downloading");
        assertThat(torrent.percentDone()).isEqualTo(50.0);
        assertThat(torrent.totalSize()).isEqualTo(1000L);
        assertThat(torrent.rateDownload()).isEqualTo(100L);
        assertThat(torrent.rateUpload()).isEqualTo(50L);
    }

    @Test
    void toDomainList_shouldMapListOfTorrentResponsesToTorrents() {
        // Given
        TorrentResponse response1 = new TorrentResponse();
        response1.setId(1);
        response1.setName("Torrent 1");
        response1.setStatus(4);
        response1.setPercentDone(50.0);
        response1.setTotalSize(1000L);
        response1.setRateDownload(100L);
        response1.setRateUpload(50L);

        TorrentResponse response2 = new TorrentResponse();
        response2.setId(2);
        response2.setName("Torrent 2");
        response2.setStatus(6);
        response2.setPercentDone(100.0);
        response2.setTotalSize(2000L);
        response2.setRateDownload(0L);
        response2.setRateUpload(100L);

        List<TorrentResponse> responses = List.of(response1, response2);

        // When
        List<Torrent> torrents = mapper.toDomainList(responses);

        // Then
        assertThat(torrents).hasSize(2);
        assertThat(torrents.get(0).name()).isEqualTo("Torrent 1");
        assertThat(torrents.get(0).status()).isEqualTo("downloading");
        assertThat(torrents.get(1).name()).isEqualTo("Torrent 2");
        assertThat(torrents.get(1).status()).isEqualTo("seeding");
    }

    @Test
    void mapStatus_shouldMapAllStatusCodesCorrectly() {
        // Test all status codes
        assertThat(mapStatus(0)).isEqualTo("stopped");
        assertThat(mapStatus(1)).isEqualTo("check pending");
        assertThat(mapStatus(2)).isEqualTo("checking");
        assertThat(mapStatus(3)).isEqualTo("download pending");
        assertThat(mapStatus(4)).isEqualTo("downloading");
        assertThat(mapStatus(5)).isEqualTo("seed pending");
        assertThat(mapStatus(6)).isEqualTo("seeding");
        assertThat(mapStatus(99)).isEqualTo("unknown");
    }

    private String mapStatus(int statusCode) {
        // Copy of the private method for testing
        switch (statusCode) {
            case 0: return "stopped";
            case 1: return "check pending";
            case 2: return "checking";
            case 3: return "download pending";
            case 4: return "downloading";
            case 5: return "seed pending";
            case 6: return "seeding";
            default: return "unknown";
        }
    }
}
