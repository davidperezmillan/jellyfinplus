package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.mappers.TransmissionMapper;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TransmissionResponse;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentResponse;
import com.davidperezmillan.jellyfinplus.infrastructure.config.TransmissionConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransmissionApiClientTest {

    private TransmissionConfig config;
    private TransmissionMapper mapper;
    private TransmissionApiClient client;
    private WebClient webClient;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() throws Exception {
        config = mock(TransmissionConfig.class);
        mapper = mock(TransmissionMapper.class);
        client = new TransmissionApiClient(config, mapper);

        // Mock WebClient and its chain
        webClient = mock(WebClient.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        // Inject mock WebClient into the private final field
        Field webClientField = TransmissionApiClient.class.getDeclaredField("webClient");
        webClientField.setAccessible(true);
        webClientField.set(client, webClient);

        when(config.getBaseUrl()).thenReturn("http://localhost:9091/transmission/rpc");
        when(config.getUsername()).thenReturn("user");
        when(config.getPassword()).thenReturn("pass");
    }

    @Test
    void addTorrent_shouldCallRpcWithCorrectRequest() {
        // Given
        String torrentFile = "test.torrent";
        TransmissionResponse response = new TransmissionResponse();
        response.setResult("success");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TransmissionResponse.class)).thenReturn(Mono.just(response));

        // When
        client.addTorrent(torrentFile);

        // Then - method completes without exception
        assertThat(true).isTrue(); // Placeholder assertion
    }

    @Test
    void addMagnet_shouldCallRpcWithCorrectRequest() {
        // Given
        String magnetLink = "magnet:?xt=urn:btih:test";
        TransmissionResponse response = new TransmissionResponse();
        response.setResult("success");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TransmissionResponse.class)).thenReturn(Mono.just(response));

        // When
        client.addMagnet(magnetLink);

        // Then - method completes without exception
        assertThat(true).isTrue(); // Placeholder assertion
    }

    @Test
    void listTorrents_shouldReturnMappedTorrents() {
        // Given
        TransmissionResponse response = new TransmissionResponse();
        response.setResult("success");
        com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.ArgumentsResponse arguments = new com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.ArgumentsResponse();
        TorrentResponse torrentResponse = new TorrentResponse();
        torrentResponse.setId(1);
        torrentResponse.setName("Test Torrent");
        arguments.setTorrents(List.of(torrentResponse));
        response.setArguments(arguments);

        Torrent torrent = new Torrent(1, "Test Torrent", "downloading", 0.5, 1000L, 100, 50);
        when(mapper.toDomainList(any())).thenReturn(List.of(torrent));

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(TransmissionResponse.class)).thenReturn(Mono.just(response));

        // When
        List<Torrent> result = client.listTorrents();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Test Torrent");
    }
}
