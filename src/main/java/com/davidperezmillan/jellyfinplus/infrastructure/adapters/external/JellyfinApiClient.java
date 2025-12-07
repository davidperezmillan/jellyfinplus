package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.infrastructure.config.JellyfinConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class JellyfinApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JellyfinConfig config;

    public JellyfinApiClient(JellyfinConfig config) {
        this.config = config;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Emby-Token", config.getToken());
        return headers;
    }

    private String getUserId() {
        if (config.getUserId() != null && !config.getUserId().isEmpty()) {
            return config.getUserId();
        }
        if (config.getUserName() != null && !config.getUserName().isEmpty()) {
            // Find user by name
            String url = config.getBaseUrl() + "/Users";
            HttpEntity<String> entity = new HttpEntity<>(getHeaders());
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            try {
                JsonNode root = objectMapper.readTree(response.getBody());
                for (JsonNode user : root) {
                    if (config.getUserName().equals(user.get("Name").asText())) {
                        return user.get("Id").asText();
                    }
                }
                throw new RuntimeException("User not found: " + config.getUserName());
            } catch (Exception e) {
                throw new RuntimeException("Error getting user id", e);
            }
        }
        // Default to first user
        String url = config.getBaseUrl() + "/Users";
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.get(0).get("Id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error getting user id", e);
        }
    }

    public List<Series> getSeries() {
        String userId = getUserId();
        String url = config.getBaseUrl() + "/Users/" + userId + "/Items?IncludeItemTypes=Series&Recursive=true";
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode items = root.get("Items");
            List<Series> series = new ArrayList<>();
            for (JsonNode item : items) {
                String id = item.get("Id").asText();
                String name = item.get("Name").asText();
                String overview = item.get("Overview") != null ? item.get("Overview").asText() : "";
                boolean downloaded = item.get("Path") != null;
                series.add(new Series(id, name, overview, downloaded));
            }
            return series;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing series", e);
        }
    }

    public List<Episode> getEpisodes(String seriesId) {
        String userId = getUserId();
        String url = config.getBaseUrl() + "/Shows/" + seriesId + "/Episodes?UserId=" + userId;
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        try {
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
                episodes.add(new Episode(id, name, overview, seriesId, seasonNumber, episodeNumber, downloaded));
            }
            return episodes;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing episodes", e);
        }
    }

    public List<Episode> getAllEpisodes() {
        String userId = getUserId();
        String url = config.getBaseUrl() + "/Users/" + userId + "/Items?IncludeItemTypes=Episode&Recursive=true";
        HttpEntity<String> entity = new HttpEntity<>(getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        try {
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
                episodes.add(new Episode(id, name, overview, seriesId, seasonNumber, episodeNumber, downloaded));
            }
            return episodes;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing episodes", e);
        }
    }
}
