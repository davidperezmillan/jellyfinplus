package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.ports.EpisodeRepository;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.external.JellyfinApiClient;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JellyfinEpisodeRepository implements EpisodeRepository {

    private final JellyfinApiClient apiClient;

    public JellyfinEpisodeRepository(JellyfinApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<Episode> findBySeriesId(String seriesId) {
        return apiClient.getEpisodes(seriesId);
    }

    @Override
    public List<Episode> findDownloaded() {
        return apiClient.getAllEpisodes().stream().filter(Episode::isDownloaded).toList();
    }
}
