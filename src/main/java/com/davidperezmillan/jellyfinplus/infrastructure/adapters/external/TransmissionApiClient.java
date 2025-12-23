package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.mappers.TransmissionMapper;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request.TransmissionRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request.ArgumentsRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TransmissionResponse;
import com.davidperezmillan.jellyfinplus.infrastructure.config.TransmissionConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class TransmissionApiClient {

    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().responseTimeout(Duration.ofSeconds(30))
            ))
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10 MB
            .build();
    private final TransmissionConfig config;
    private final TransmissionMapper mapper;
    private String sessionId;

    public TransmissionApiClient(TransmissionConfig config, TransmissionMapper mapper) {
        this.config = config;
        this.mapper = mapper;
        log.info("Transmission API Client initialized with base URL: {}", config.getBaseUrl());
    }

    public void addTorrent(String torrentFile) {
        log.debug("Adding torrent file: {}", torrentFile);
        TransmissionRequest request = createAddRequest(torrentFile);
        callRpc(request);
        log.info("Torrent added successfully");
    }

    public void addMagnet(String magnetLink) {
        log.debug("Adding magnet link: {}", magnetLink);
        TransmissionRequest request = new TransmissionRequest();
        request.setMethod("torrent-add");
        ArgumentsRequest arguments = new ArgumentsRequest();
        arguments.setFilename(magnetLink);
        arguments.setDownloadDir("/downloads/complete");

        request.setArguments(arguments);
        // llamada RPC
        callRpc(request);
        log.info("Torrent added successfully");
    }

    public List<Torrent> listTorrents() {
        log.debug("Listing torrents");
        TransmissionRequest request = createListRequest();
        TransmissionResponse response = callRpc(request);
        List<Torrent> torrents = mapper.toDomainList(response.getArguments().getTorrents());
        log.info("Retrieved {} torrents", torrents.size());
        return torrents;
    }

    private TransmissionRequest createAddRequest(String filename) {
        TransmissionRequest request = new TransmissionRequest();
        request.setMethod("torrent-add");

        ArgumentsRequest args = new ArgumentsRequest();
        args.setFilename(filename);
        request.setArguments(args);

        return request;
    }

    private TransmissionRequest createListRequest() {
        TransmissionRequest request = new TransmissionRequest();
        request.setMethod("torrent-get");

        ArgumentsRequest args = new ArgumentsRequest();
        args.setFields(new String[]{"id", "name", "status", "percentDone", "totalSize", "rateDownload", "rateUpload"});
        request.setArguments(args);

        return request;
    }

    private TransmissionResponse callRpc(TransmissionRequest request) {
        if  (sessionId == null) {
            log.debug("No session ID, making initial request to obtain it");
            getSessionId();
        }
        try {
            log.info("Calling {} ", request.getMethod());
            HttpHeaders headers = createHeaders();
            headers.set("X-Transmission-Session-Id", sessionId);

            log.debug("Calling Transmission API with session ID: {}", sessionId);
            // trazame lo que se va a enviar

            TransmissionResponse requestSpec = webClient.post()
                    .uri(config.getBaseUrl())
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(TransmissionResponse.class)
                    .block();

            // log the request and response
            ObjectMapper mapper = new ObjectMapper();
            String requestJson = mapper.writeValueAsString(request);
            String responseJson = mapper.writeValueAsString(requestSpec);
            log.debug("Transmission RPC Request: {}", URLDecoder.decode(requestJson, StandardCharsets.UTF_8.name()));
            log.debug("Transmission RPC Response: {}", URLDecoder.decode(responseJson, StandardCharsets.UTF_8.name()));

            log.debug("RPC call successful: {}", requestSpec.getResult());
            return requestSpec;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 409) {
                // Session ID conflict, extract new session ID
                String newSessionId = e.getHeaders().getFirst("X-Transmission-Session-Id");
                if (newSessionId != null) {
                    log.debug("Updating session ID: {}", newSessionId);
                    sessionId = newSessionId;
                    // Retry with new session ID
                    return callRpc(request);
                }
            }
            log.error("RPC call failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error calling Transmission RPC: {}", e.getMessage());
            throw new RuntimeException("Failed to call Transmission RPC", e);
        }
    }


    private void getSessionId() {
        try {
            HttpHeaders headers = createHeaders();
            TransmissionResponse response = webClient.post()
                    .uri(config.getBaseUrl())
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .bodyToMono(TransmissionResponse.class)
                    .block();
            sessionId = response != null ? response.getResult() : null;
        } catch (WebClientResponseException.Conflict e) {
            sessionId = e.getHeaders().getFirst("X-Transmission-Session-Id");
            log.info("Session ID by Error: {}", sessionId);
        } catch (Exception e) {
            log.error("Transmission no está disponible.", e);
        }
    }


    private HttpHeaders createHeaders() {
        String auth = config.getUsername()+ ":" + config.getPassword();
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }
}
