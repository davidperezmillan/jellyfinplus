package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@Tag(name = "Episodios", description = "Operaciones relacionadas con episodios de series en Jellyfin")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping("/series/{seriesId}")
    @Operation(summary = "Obtener episodios de una serie", description = "Recupera todos los episodios de una serie específica")
    public List<Episode> getEpisodesBySeries(@Parameter(description = "ID de la serie") @PathVariable String seriesId) {
        return episodeService.getEpisodesBySeries(seriesId);
    }

    @GetMapping("/downloaded")
    @Operation(summary = "Obtener episodios descargados", description = "Recupera todos los episodios que están descargados localmente")
    public List<Episode> getDownloadedEpisodes() {
        return episodeService.getDownloadedEpisodes();
    }
}
