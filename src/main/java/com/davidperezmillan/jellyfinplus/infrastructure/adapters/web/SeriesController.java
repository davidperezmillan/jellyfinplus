package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/series")
@Tag(name = "Series", description = "Operaciones relacionadas con series de Jellyfin")
public class SeriesController {

    private static final Logger log = LoggerFactory.getLogger(SeriesController.class);

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
        log.info("SeriesController initialized");
    }

    @GetMapping
    @Operation(summary = "Obtener todas las series", description = "Recupera la lista completa de series disponibles en Jellyfin")
    public List<Series> getAllSeries() {
        log.info("REST API - GET /api/series");
        List<Series> series = seriesService.getAllSeries();
        log.info("REST API - Returning {} series", series.size());
        return series;
    }

    @GetMapping("/downloaded")
    @Operation(summary = "Obtener series descargadas", description = "Recupera solo las series que están descargadas localmente")
    public List<Series> getDownloadedSeries() {
        log.info("REST API - GET /api/series/downloaded");
        List<Series> series = seriesService.getDownloadedSeries();
        log.info("REST API - Returning {} downloaded series", series.size());
        return series;
    }
}
