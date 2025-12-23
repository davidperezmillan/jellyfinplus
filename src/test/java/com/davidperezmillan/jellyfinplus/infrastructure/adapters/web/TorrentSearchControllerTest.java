package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.TorrentSearchService;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TorrentSearchController.class)
class TorrentSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TorrentSearchService torrentSearchService;

    @Test
    void searchTorrents_shouldReturnResults_whenAllParametersProvided() throws Exception {

        TorrentSearchResult torrentSearchResult = new TorrentSearchResult();
        torrentSearchResult.setTorrent_url("magnet:?xt=urn:btih:123");
        torrentSearchResult.setTitle("Test Torrent");


        // Given
        List<TorrentSearchResult> results = List.of(
                torrentSearchResult
        );
        when(torrentSearchService.searchTorrents("1337x", "72", "1", 10)).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/torrent-search/search")
                        .param("site", "1337x")
                        .param("category", "72")
                        .param("page", "1")
                        .param("maxResults", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Torrent"));
    }

    @Test
    void searchTorrents_shouldUseDefaultValues_whenOptionalParametersNotProvided() throws Exception {
        // Given
        TorrentSearchResult torrentSearchResult = new TorrentSearchResult();
        torrentSearchResult.setTorrent_url("magnet:?xt=urn:btih:123");
        torrentSearchResult.setTitle("Test Torrent");
        List<TorrentSearchResult> results = List.of(
                torrentSearchResult
        );
        when(torrentSearchService.searchTorrents("1337x", "72", "1", 10)).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/torrent-search/search")
                        .param("site", "1337x")
                        .param("category", "72"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchTorrents_shouldUseProvidedPage_whenPageParameterProvided() throws Exception {
        // Given
        TorrentSearchResult torrentSearchResult = new TorrentSearchResult();
        torrentSearchResult.setTorrent_url("magnet:?xt=urn:btih:123");
        torrentSearchResult.setTitle("Test Torrent");
        List<TorrentSearchResult> results = List.of(
                torrentSearchResult
        );
        when(torrentSearchService.searchTorrents("1337x", "72", "2", 10)).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/torrent-search/search")
                        .param("site", "1337x")
                        .param("category", "72")
                        .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchTorrents_shouldUseProvidedMaxResults_whenMaxResultsParameterProvided() throws Exception {
        // Given
        TorrentSearchResult torrentSearchResult = new TorrentSearchResult();
        torrentSearchResult.setTorrent_url("magnet:?xt=urn:btih:123");
        torrentSearchResult.setTitle("Test Torrent");
        List<TorrentSearchResult> results = List.of(
                torrentSearchResult
        );
        when(torrentSearchService.searchTorrents("1337x", "72", "1", 5)).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/torrent-search/search")
                        .param("site", "1337x")
                        .param("category", "72")
                        .param("maxResults", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchTorrents_shouldReturnEmptyList_whenNoResultsFound() throws Exception {
        // Given
        List<TorrentSearchResult> results = List.of();
        when(torrentSearchService.searchTorrents(anyString(), anyString(), anyString(), anyInt())).thenReturn(results);

        // When & Then
        mockMvc.perform(get("/api/torrent-search/search")
                        .param("site", "1337x")
                        .param("category", "72"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
