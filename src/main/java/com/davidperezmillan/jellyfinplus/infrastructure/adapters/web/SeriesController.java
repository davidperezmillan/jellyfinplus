package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/series")
@Tag(name = "Series", description = "Operaciones relacionadas con series de Jellyfin")
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las series", description = "Recupera la lista completa de series disponibles en Jellyfin")
    public List<Series> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/downloaded")
    @Operation(summary = "Obtener series descargadas", description = "Recupera solo las series que están descargadas localmente")
    public List<Series> getDownloadedSeries() {
        return seriesService.getDownloadedSeries();
    }
}
