package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.infrastructure.adapters.external.TorrentSearchApiClient;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request.TorrentSearchRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TorrentSearchRepositoryImplTest {

    @Mock
    private TorrentSearchApiClient apiClient;

    @InjectMocks
    private TorrentSearchRepositoryImpl repository;

    @Test
    void searchTorrents_shouldReturnResults_whenApiClientReturnsResults() {
        // Given
        TorrentSearchResult result = new TorrentSearchResult();
        result.setTitle("Test Torrent");
        List<TorrentSearchResult> expectedResults = List.of(result);

        when(apiClient.searchTorrents(any(TorrentSearchRequest.class)))
                .thenReturn(expectedResults);

        // When
        List<TorrentSearchResult> actualResults = repository.searchTorrents("1337x", "72", "1", 10);

        // Then
        assertThat(actualResults).isEqualTo(expectedResults);
    }

    @Test
    void searchTorrents_shouldReturnEmptyList_whenApiClientReturnsEmptyList() {
        // Given
        when(apiClient.searchTorrents(any(TorrentSearchRequest.class)))
                .thenReturn(List.of());

        // When
        List<TorrentSearchResult> actualResults = repository.searchTorrents("site", "cat", "sub", 5);

        // Then
        assertThat(actualResults).isEmpty();
    }
}
