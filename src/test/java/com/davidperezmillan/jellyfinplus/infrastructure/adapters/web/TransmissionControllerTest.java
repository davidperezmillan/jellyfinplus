package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.TorrentSearchService;
import com.davidperezmillan.jellyfinplus.application.services.TransmissionService;
import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.request.TransmissionMagnetRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.response.TransmissionRestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransmissionControllerTest {

    @Mock
    private TransmissionService transmissionService;

    @Mock
    private TorrentSearchService torrentSearchService;

    @InjectMocks
    private TransmissionController controller;

    @Test
    void addTorrentsFromSearch_shouldReturnSuccessMessage_whenTorrentsAreAdded() {
        // Given
        TorrentSearchResult result1 = new TorrentSearchResult();
        result1.setTitle("Torrent 1");
        result1.setDownload_link("magnet:?xt=urn:btih:123");

        TorrentSearchResult result2 = new TorrentSearchResult();
        result2.setTitle("Torrent 2");
        result2.setDownload_link("magnet:?xt=urn:btih:456");

        List<TorrentSearchResult> searchResults = List.of(result1, result2);

        when(torrentSearchService.searchTorrents("1337x", "72", "1", 10))
                .thenReturn(searchResults);
        doNothing().when(transmissionService).addMagnet(anyString());

        // When
        ResponseEntity<String> response = controller.addTorrentsFromSearch();

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Se añadieron 2 de 2 torrents encontrados.");
    }

    @Test
    void addTorrentsFromSearch_shouldSkipTorrentsWithoutMagnetLink() {
        // Given
        TorrentSearchResult result1 = new TorrentSearchResult();
        result1.setTitle("Torrent 1");
        result1.setDownload_link("magnet:?xt=urn:btih:123");

        TorrentSearchResult result2 = new TorrentSearchResult();
        result2.setTitle("Torrent 2");
        result2.setDownload_link(null); // No magnet link

        List<TorrentSearchResult> searchResults = List.of(result1, result2);

        when(torrentSearchService.searchTorrents("1337x", "72", "1", 10))
                .thenReturn(searchResults);
        doNothing().when(transmissionService).addMagnet(anyString());

        // When
        ResponseEntity<String> response = controller.addTorrentsFromSearch();

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Se añadieron 1 de 2 torrents encontrados.");
    }

    @Test
    void addTorrentsFromSearch_shouldReturnErrorMessage_whenSearchFails() {
        // Given
        when(torrentSearchService.searchTorrents("1337x", "72", "1", 10))
                .thenThrow(new RuntimeException("Search failed"));

        // When
        ResponseEntity<String> response = controller.addTorrentsFromSearch();

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isEqualTo("Error al buscar o añadir torrents: Search failed");
    }



    @Test
    void addTorrent_shouldReturnSuccessMessage() {
        // Given
        String torrentFile = "base64encodedtorrent";
        doNothing().when(transmissionService).addTorrent(torrentFile);

        // When
        ResponseEntity<String> response = controller.addTorrent(torrentFile);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Torrent añadido correctamente");
    }

    @Test
    void addMagnet_shouldReturnSuccessMessage() {
        // Given
        TransmissionMagnetRequest request = new TransmissionMagnetRequest();
        request.setMagnet("magnet:?xt=urn:btih:123");
        doNothing().when(transmissionService).addMagnet(request.getMagnet());

        // When
        ResponseEntity<String> response = controller.addMagnet(request);

        // Then
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Magnet añadido correctamente");
    }
}
