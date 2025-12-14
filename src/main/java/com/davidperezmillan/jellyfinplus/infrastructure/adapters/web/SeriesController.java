package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.EpisodeService;
import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.model.SeriesWithEpisodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para operaciones sobre series de Jellyfin.
 *
 * <p>Endpoints disponibles:
 * <ul>
 *   <li>GET /api/series - devuelve todas las series.</li>
 *   <li>GET /api/series/with-episodes - devuelve series con sus episodios y el campo {@code nextEpisode}.</li>
 *   <li>GET /api/series/with-episodes/search - buscar series por título y devolver sus episodios y {@code nextEpisode}.</li>
 * </ul>
 *
 * <p>Descripción de {@code nextEpisode}:
 * El campo {@code nextEpisode} representa el siguiente episodio a conseguir para la serie.
 * La determinación de este siguiente episodio se realiza en {@code EpisodeService#getNextEpisode(seriesId)}
 * y sigue la regla: elegir el siguiente episodio de la última temporada disponible,
 * incrementando el número de episodio en 1 dentro de esa temporada.
 */
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

    /**
     * Recupera la lista completa de series disponibles en Jellyfin.
     *
     * @return lista de {@link Series}
     */
    @GetMapping
    @Operation(summary = "Obtener todas las series", description = "Recupera la lista completa de series disponibles en Jellyfin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de series devuelta correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<Series> getAllSeries() {
        return seriesService.getAllSeries();
    }

    /**
     * Recupera todas las series y sus episodios asociados.
     *
     * @param unwatched si es true, solo devuelve episodios no vistos; si es false o null, devuelve todos los episodios
     * @return lista de {@link SeriesWithEpisodes} que incluye el campo {@code nextEpisode}
     */
    @GetMapping("/with-episodes")
    @Operation(summary = "Obtener series con episodios", description = "Recupera todas las series y sus episodios asociados. El campo nextEpisode indica el siguiente episodio a conseguir según la lógica del servicio de episodios.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Series con episodios devueltas correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<SeriesWithEpisodes> getSeriesWithEpisodes(
            @Parameter(description = "Si true, devuelve solo episodios no vistos") @RequestParam(value = "unwatched", required = false) Boolean unwatched) {
        boolean onlyUnwatched = Boolean.TRUE.equals(unwatched);
        return seriesService.getAllSeries().stream()
                .map(s -> new SeriesWithEpisodes(s,
                        onlyUnwatched ? episodeService.getUnwatchedEpisodesBySeries(s.id()) : episodeService.getEpisodesBySeries(s.id()),
                        episodeService.getNextEpisode(s.id())
                ))
                .toList();
    }

    /**
     * Buscar series cuyo título coincida (contains, case-insensitive) y devolver sus episodios.
     *
     * @param title     término de búsqueda (seignifica contain, case-insensitive)
     * @param unwatched si es true, solo devuelve episodios no vistos; si es false o null, devuelve todos los episodios
     * @return lista filtrada de {@link SeriesWithEpisodes}
     */
    @GetMapping("/with-episodes/search")
    @Operation(summary = "Buscar series por título con episodios", description = "Buscar series cuyo título coincida (contains, case-insensitive) y devolver sus episodios. Incluye el campo nextEpisode calculado por el servicio de episodios.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultados de búsqueda devueltos correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<SeriesWithEpisodes> getSeriesWithEpisodesByTitle(
            @Parameter(description = "Título o fragmento de título a buscar", required = true) @RequestParam("title") String title,
            @Parameter(description = "Si true, devuelve solo episodios no vistos") @RequestParam(value = "unwatched", required = false) Boolean unwatched) {
        String normalized = title == null ? "" : title.trim().toLowerCase();
        boolean onlyUnwatched = Boolean.TRUE.equals(unwatched);
        return seriesService.getAllSeries().stream()
                .filter(s -> s.name() != null && s.name().toLowerCase().contains(normalized))
                .map(s -> new SeriesWithEpisodes(s,
                        onlyUnwatched ? episodeService.getUnwatchedEpisodesBySeries(s.id()) : episodeService.getEpisodesBySeries(s.id()),
                        episodeService.getNextEpisode(s.id())
                ))
                .toList();
    }

}
