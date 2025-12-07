package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.infrastructure.config.JellyfinConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JellyfinApiClient {


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JellyfinConfig config;

    public JellyfinApiClient(JellyfinConfig config) {
        this.config = config;
        log.info("Jellyfin API Client initialized with base URL: {}", config.getBaseUrl());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Emby-Token", config.getToken());
        log.debug("Headers configured with X-Emby-Token");
        return headers;
    }

    private String getUserId() {
        log.debug("Obtaining user ID");
        if (config.getUserId() != null && !config.getUserId().isEmpty()) {
            log.debug("Using configured user ID: {}", config.getUserId());
            return config.getUserId();
        }
        if (config.getUserName() != null && !config.getUserName().isEmpty()) {
            log.info("Searching for user by name: {}", config.getUserName());
            // Find user by name
            String url = config.getBaseUrl() + "/Users";
            log.debug("GET request to: {}", url);
            HttpEntity<String> entity = new HttpEntity<>(getHeaders());
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                log.debug("Response status: {}", response.getStatusCode());
                log.trace("Response body: {}", response.getBody());

                JsonNode root = objectMapper.readTree(response.getBody());
                for (JsonNode user : root) {
                    if (config.getUserName().equals(user.get("Name").asText())) {
                        String userId = user.get("Id").asText();
                        log.info("User '{}' found with ID: {}", config.getUserName(), userId);
                        return userId;
                    }
                }
                log.error("User not found: {}", config.getUserName());
                throw new RuntimeException("User not found: " + config.getUserName());
            } catch (Exception e) {
                log.error("Error getting user id for user: {}", config.getUserName(), e);
                throw new RuntimeException("Error getting user id", e);
            }
        }
        // Default to first user
        log.info("No user ID or name configured, using first available user");
        String url = config.getBaseUrl() + "/Users";
        log.debug("GET request to: {}", url);
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("Response status: {}", response.getStatusCode());
            log.trace("Response body: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            String userId = root.get(0).get("Id").asText();
            log.info("Using first user with ID: {}", userId);
            return userId;
        } catch (Exception e) {
            log.error("Error getting default user id", e);
            throw new RuntimeException("Error getting user id", e);
        }
    }

    public List<Series> getSeries() {
        log.info("Fetching all series from Jellyfin");
        String userId = getUserId();
        String url = config.getBaseUrl() + "/Users/" + userId + "/Items?IncludeItemTypes=Series&Recursive=true";
        log.debug("GET request to: {}", url);

        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("Response status: {}", response.getStatusCode());
            log.trace("Response body: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.get("Items");
            List<Series> series = new ArrayList<>();
            for (JsonNode item : items) {
                String id = item.get("Id").asText();
                String name = item.get("Name").asText();
                String overview = item.get("Overview") != null ? item.get("Overview").asText() : "";
                boolean downloaded = item.get("Path") != null;
                series.add(new Series(id, name, overview, downloaded));
                log.trace("Added series: {} (ID: {}, Downloaded: {})", name, id, downloaded);
            }
            log.info("Successfully fetched {} series", series.size());
            return series;
        } catch (Exception e) {
            log.error("Error fetching series from Jellyfin", e);
            throw new RuntimeException("Error parsing series", e);
        }
    }

    public List<Episode> getEpisodes(String seriesId) {
        log.info("Fetching episodes for series ID: {}", seriesId);
        String userId = getUserId();
        String url = config.getBaseUrl() + "/Shows/" + seriesId + "/Episodes?UserId=" + userId;
        log.debug("GET request to: {}", url);

        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("Response status: {}", response.getStatusCode());
            log.trace("Response body: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.get("Items");
            List<Episode> episodes = new ArrayList<>();
            for (JsonNode item : items) {
                String id = item.get("Id").asText();
                String name = item.get("Name").asText();
                String overview = item.get("Overview") != null ? item.get("Overview").asText() : "";
                int seasonNumber = item.get("ParentIndexNumber") != null ? item.get("ParentIndexNumber").asInt() : 0;
                int episodeNumber = item.get("IndexNumber") != null ? item.get("IndexNumber").asInt() : 0;
                boolean downloaded = item.get("Path") != null;
                boolean played = false;
                if (item.has("UserData") && item.get("UserData").has("Played")) {
                    played = item.get("UserData").get("Played").asBoolean();
                }
                episodes.add(new Episode(id, name, overview, seriesId, seasonNumber, episodeNumber, downloaded, played));
                log.trace("Added episode: S{}E{} - {} (ID: {}, Downloaded: {}, Played: {})",
                        seasonNumber, episodeNumber, name, id, downloaded, played);
            }
            log.info("Successfully fetched {} episodes for series ID: {}", episodes.size(), seriesId);
            return episodes;
        } catch (Exception e) {
            log.error("Error fetching episodes for series ID: {}", seriesId, e);
            throw new RuntimeException("Error parsing episodes", e);
        }
    }

    public List<Episode> getAllEpisodes() {
        log.info("Fetching all episodes from Jellyfin");
        String userId = getUserId();
        String url = config.getBaseUrl() + "/Users/" + userId + "/Items?IncludeItemTypes=Episode&Recursive=true";
        log.debug("GET request to: {}", url);

        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            log.debug("Response status: {}", response.getStatusCode());
            log.trace("Response body: {}", response.getBody());

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.get("Items");
            List<Episode> episodes = new ArrayList<>();
            for (JsonNode item : items) {
                String id = item.get("Id").asText();
                String name = item.get("Name").asText();
                String overview = item.get("Overview") != null ? item.get("Overview").asText() : "";
                String seriesId = item.get("SeriesId") != null ? item.get("SeriesId").asText() : "";
                int seasonNumber = item.get("ParentIndexNumber") != null ? item.get("ParentIndexNumber").asInt() : 0;
                int episodeNumber = item.get("IndexNumber") != null ? item.get("IndexNumber").asInt() : 0;
                boolean downloaded = item.get("Path") != null;
                boolean played = false;
                if (item.has("UserData") && item.get("UserData").has("Played")) {
                    played = item.get("UserData").get("Played").asBoolean();
                }
                episodes.add(new Episode(id, name, overview, seriesId, seasonNumber, episodeNumber, downloaded, played));
                log.trace("Added episode: S{}E{} - {} (Series ID: {}, Downloaded: {}, Played: {})",
                        seasonNumber, episodeNumber, name, seriesId, downloaded, played);
            }
            log.info("Successfully fetched {} episodes in total", episodes.size());
            return episodes;
        } catch (Exception e) {
            log.error("Error fetching all episodes from Jellyfin", e);
            throw new RuntimeException("Error parsing episodes", e);
        }
    }
}
