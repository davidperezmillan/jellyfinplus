package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.ports.EpisodeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EpisodeService {

    private final EpisodeRepository episodeRepository;

    public EpisodeService(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    public List<Episode> getEpisodesBySeries(String seriesId) {
        return episodeRepository.findBySeriesId(seriesId);
    }

    public List<Episode> getUnwatchedEpisodesBySeries(String seriesId) {
        return getEpisodesBySeries(seriesId).stream()
                .filter(episode -> !episode.isPlayed())
                .toList();
    }
}
