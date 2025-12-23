package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request.TorrentSearchRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchData;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResponse;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TorrentSearchApiClientTest {

    private TorrentSearchApiClient torrentSearchApiClient;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() throws Exception {
        torrentSearchApiClient = new TorrentSearchApiClient();

        // Mock WebClient and its chain
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        // Inject mock WebClient into the private final field
        Field webClientField = TorrentSearchApiClient.class.getDeclaredField("webClient");
        webClientField.setAccessible(true);
        webClientField.set(torrentSearchApiClient, webClient);
    }

    @Test
    void searchTorrents_shouldReturnResults_whenResponseIsValid() {
        // Given
        TorrentSearchRequest request = new TorrentSearchRequest();
        request.setSite("1337x");
        TorrentSearchRequest.Criteria criteria = new TorrentSearchRequest.Criteria();
        criteria.setCategory("72");
        criteria.setMaxResults(10);
        request.setCriteria(criteria);

        TorrentSearchResult result = new TorrentSearchResult();
        result.setTitle("Test Torrent");
        result.setDownload_link("magnet:?test");

        TorrentSearchData data = new TorrentSearchData();
        data.setTorrents(List.of(result));

        TorrentSearchResponse response = new TorrentSearchResponse();
        response.setData(data);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TorrentSearchResponse.class)).thenReturn(Mono.just(response));

        // When
        List<TorrentSearchResult> results = torrentSearchApiClient.searchTorrents(request);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Test Torrent");
        assertThat(results.get(0).getDownload_link()).isEqualTo("magnet:?test");
    }

    @Test
    void searchTorrents_shouldReturnEmptyList_whenResponseIsNull() {
        // Given
        TorrentSearchRequest request = new TorrentSearchRequest();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TorrentSearchResponse.class)).thenReturn(Mono.empty());

        // When
        List<TorrentSearchResult> results = torrentSearchApiClient.searchTorrents(request);

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    void searchTorrents_shouldReturnEmptyList_whenDataIsNull() {
        // Given
        TorrentSearchRequest request = new TorrentSearchRequest();
        TorrentSearchResponse response = new TorrentSearchResponse();
        // data is null

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TorrentSearchResponse.class)).thenReturn(Mono.just(response));

        // When
        List<TorrentSearchResult> results = torrentSearchApiClient.searchTorrents(request);

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    void searchTorrents_shouldThrowException_whenWebClientFails() {
        // Given
        TorrentSearchRequest request = new TorrentSearchRequest();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TorrentSearchResponse.class)).thenReturn(Mono.error(new RuntimeException("Network error")));

        // When & Then
        assertThatThrownBy(() -> torrentSearchApiClient.searchTorrents(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to search torrents");
    }
}
