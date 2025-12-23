package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.TorrentSearchService;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para operaciones de búsqueda de torrents.
 *
 * <p>Este controlador proporciona endpoints para buscar torrents en sitios externos.</p>
 *
 * @author David Perez Millan
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/torrent-search")
@Tag(name = "Torrent Search", description = "Operaciones de búsqueda de torrents")
public class TorrentSearchController {

    private final TorrentSearchService torrentSearchService;

    public TorrentSearchController(TorrentSearchService torrentSearchService) {
        this.torrentSearchService = torrentSearchService;
    }

    /**
     * Busca torrents en un sitio específico con criterios dados.
     *
     * @param site El sitio de búsqueda (ej. "1337x")
     * @param category La categoría
     * @param page Las páginas (opcional, por defecto "1")
     * @param maxResults Número máximo de resultados (opcional, por defecto 10)
     * @return Lista de resultados de búsqueda de torrents
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar torrents", description = "Busca torrents en un sitio específico con los criterios proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<TorrentSearchResult>> searchTorrents(
            @Parameter(description = "Sitio de búsqueda", example = "1337x") @RequestParam String site,
            @Parameter(description = "Categoría", example = "72") @RequestParam String category,
            @Parameter(description = "Páginas (opcional)", example = "1") @RequestParam(required = false, defaultValue = "1") String page,
            @Parameter(description = "Número máximo de resultados (opcional)", example = "10") @RequestParam Optional<Integer> maxResults) {

        int actualMaxResults = maxResults.orElse(10);
        List<TorrentSearchResult> results = torrentSearchService.searchTorrents(site, category, page, actualMaxResults);
        log.info("Se encontraron {} resultados de búsqueda en el sitio {}", results.size(), site);
        return ResponseEntity.ok(results);
    }
}
