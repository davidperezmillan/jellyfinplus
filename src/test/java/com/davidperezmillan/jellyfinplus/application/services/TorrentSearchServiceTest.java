package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.ports.TorrentSearchRepository;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TorrentSearchServiceTest {

    @Mock
    private TorrentSearchRepository torrentSearchRepository;

    @InjectMocks
    private TorrentSearchService torrentSearchService;

    @Test
    void searchTorrents_shouldReturnResults_whenRepositoryReturnsResults() {
        // Given
        TorrentSearchResult result = new TorrentSearchResult();
        result.setTitle("Test Torrent");
        List<TorrentSearchResult> expectedResults = List.of(result);

        when(torrentSearchRepository.searchTorrents("1337x", "72", "1", 10))
                .thenReturn(expectedResults);

        // When
        List<TorrentSearchResult> actualResults = torrentSearchService.searchTorrents("1337x", "72", "1", 10);

        // Then
        assertThat(actualResults).isEqualTo(expectedResults);
    }

    @Test
    void searchTorrents_shouldReturnEmptyList_whenRepositoryReturnsEmptyList() {
        // Given
        when(torrentSearchRepository.searchTorrents(anyString(), anyString(), anyString(), eq(10)))
                .thenReturn(List.of());

        // When
        List<TorrentSearchResult> actualResults = torrentSearchService.searchTorrents("site", "cat", "sub", 10);

        // Then
        assertThat(actualResults).isEmpty();
    }
}
