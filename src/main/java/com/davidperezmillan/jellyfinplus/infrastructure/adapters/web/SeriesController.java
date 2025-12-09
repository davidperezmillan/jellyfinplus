package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.model.SeriesWithEpisodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@Tag(name = "Series", description = "Operaciones relacionadas con series de Jellyfin")
public class SeriesController {

    private final SeriesService seriesService;
    private final EpisodeService episodeService;

    public SeriesController(SeriesService seriesService, EpisodeService episodeService) {
        this.seriesService = seriesService;
        this.episodeService = episodeService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las series", description = "Recupera la lista completa de series disponibles en Jellyfin")
    public List<Series> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/with-episodes")
    @Operation(summary = "Obtener series con episodios", description = "Recupera todas las series y sus episodios asociados")
    public List<SeriesWithEpisodes> getSeriesWithEpisodes() {
        return seriesService.getAllSeries().stream()
                .map(s -> new SeriesWithEpisodes(s, episodeService.getEpisodesBySeries(s.id())))
                .toList();
    }



}
