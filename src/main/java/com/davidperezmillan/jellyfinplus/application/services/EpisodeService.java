package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.ports.EpisodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EpisodeService {

    private static final Logger log = LoggerFactory.getLogger(EpisodeService.class);

    private final EpisodeRepository episodeRepository;

    public EpisodeService(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
        log.info("EpisodeService initialized");
    }

    public List<Episode> getEpisodesBySeries(String seriesId) {
        log.info("Getting episodes for series ID: {}", seriesId);
        List<Episode> episodes = episodeRepository.findBySeriesId(seriesId);
        log.info("Retrieved {} episodes for series ID: {}", episodes.size(), seriesId);
        return episodes;
    }

    public List<Episode> getDownloadedEpisodes() {
        log.info("Getting downloaded episodes");
        List<Episode> episodes = episodeRepository.findDownloaded();
        log.info("Retrieved {} downloaded episodes", episodes.size());
        return episodes;
    }
}
