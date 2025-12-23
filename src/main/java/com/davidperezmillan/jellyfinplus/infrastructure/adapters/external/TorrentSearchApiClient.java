package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request.TorrentSearchRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class TorrentSearchApiClient {

    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().responseTimeout(Duration.ofSeconds(30))
            ))
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10 MB
            .build();

    public TorrentSearchApiClient() {
        log.info("Torrent Search API Client initialized");
    }

    public List<com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult> searchTorrents(TorrentSearchRequest request) {
        log.debug("Searching torrents with request: {}", request);
        try {
            TorrentSearchResponse response = webClient.post()
                    .uri("http://192.168.68.195:7002/scrape")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TorrentSearchResponse.class)
                    .block();

            if (response != null && response.getData() != null && response.getData().getTorrents() != null) {
                log.info("Retrieved {} torrents", response.getData().getTorrents().size());
                return response.getData().getTorrents();
            } else {
                log.warn("No torrents found or response is null");
                return List.of();
            }
        } catch (Exception e) {
            log.error("Error searching torrents: {}", e.getMessage());
            throw new RuntimeException("Failed to search torrents", e);
        }
    }
}
