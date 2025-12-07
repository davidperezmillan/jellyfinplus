package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping("/series/{seriesId}")
    public List<Episode> getEpisodesBySeries(@PathVariable String seriesId) {
        return episodeService.getEpisodesBySeries(seriesId);
    }

    @GetMapping("/downloaded")
    public List<Episode> getDownloadedEpisodes() {
        return episodeService.getDownloadedEpisodes();
    }
}
