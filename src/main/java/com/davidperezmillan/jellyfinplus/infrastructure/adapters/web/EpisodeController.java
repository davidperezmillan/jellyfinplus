package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "Episodios", description = "Operaciones relacionadas con episodios de series en Jellyfin")
public class EpisodeController {

    private static final Logger log = LoggerFactory.getLogger(EpisodeController.class);

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
        log.info("EpisodeController initialized");
    }

    @GetMapping("/series/{seriesId}")
    @Operation(summary = "Obtener episodios de una serie", description = "Recupera todos los episodios de una serie específica")
    public List<Episode> getEpisodesBySeries(@Parameter(description = "ID de la serie") @PathVariable String seriesId) {
        log.info("REST API - GET /api/episodes/series/{}", seriesId);
        List<Episode> episodes = episodeService.getEpisodesBySeries(seriesId);
        log.info("REST API - Returning {} episodes for series {}", episodes.size(), seriesId);
        return episodes;
    }

    @GetMapping("/downloaded")
    @Operation(summary = "Obtener episodios descargados", description = "Recupera todos los episodios que están descargados localmente")
    public List<Episode> getDownloadedEpisodes() {
        log.info("REST API - GET /api/episodes/downloaded");
        List<Episode> episodes = episodeService.getDownloadedEpisodes();
        log.info("REST API - Returning {} downloaded episodes", episodes.size());
        return episodes;
    }
}
