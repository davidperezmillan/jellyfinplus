package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web;

import com.davidperezmillan.jellyfinplus.application.services.TorrentSearchService;
import com.davidperezmillan.jellyfinplus.application.services.TransmissionService;
import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.request.TransmissionMagnetRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.response.TransmissionRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

/**
 * Controlador REST para operaciones con el cliente de torrents Transmission.
 *
 * <p>Este controlador proporciona endpoints para gestionar torrents a través de la API de Transmission.
 * Permite añadir torrents desde archivos .torrent en base64, añadir enlaces magnet, y listar
 * todos los torrents ordenados por estado.</p>
 *
 * <p>Endpoints disponibles:</p>
 * <ul>
 *   <li>POST /api/transmission/torrent - Añade un torrent desde un archivo .torrent codificado en base64</li>
 *   <li>POST /api/transmission/magnet - Añade un torrent desde un enlace magnet</li>
 *   <li>GET /api/transmission/torrents - Lista todos los torrents ordenados por estado</li>
 *   <li>POST /api/transmission/add-torrents - Busca y añade torrents automáticamente desde un sitio de búsqueda</li>
 * </ul>
 *
 * @author David Perez Millan
 * @since 1.0
 */
@RestController
@RequestMapping("/api/transmission")
@Tag(name = "Transmission", description = "Operaciones relacionadas con el cliente de torrents Transmission")
public class TransmissionController {

    private final TransmissionService transmissionService;
    private final TorrentSearchService torrentSearchService;

    public TransmissionController(TransmissionService transmissionService, TorrentSearchService torrentSearchService) {
        this.transmissionService = transmissionService;
        this.torrentSearchService = torrentSearchService;
    }

    /**
     * Añade un torrent al cliente Transmission desde un archivo .torrent codificado en base64.
     *
     * <p>Este endpoint permite añadir un nuevo torrent a Transmission proporcionando el contenido
     * del archivo .torrent codificado en formato base64. El archivo debe ser válido y contener
     * toda la información necesaria para el torrent.</p>
     *
     * @param torrentFile contenido del archivo .torrent codificado en base64. Debe ser una cadena
     *                   válida en formato base64 que represente un archivo .torrent completo.
     * @return ResponseEntity con mensaje de confirmación si el torrent se añade correctamente
     * @throws IllegalArgumentException si el archivo torrent es inválido o nulo
     */
    @PostMapping("/torrent")
    @Operation(
        summary = "Añadir torrent desde archivo",
        description = "Añade un nuevo torrent al cliente Transmission desde un archivo .torrent codificado en base64"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torrent añadido correctamente al cliente Transmission"),
            @ApiResponse(responseCode = "400", description = "El archivo torrent proporcionado es inválido o está corrupto"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud")
    })
    public ResponseEntity<String> addTorrent(
            @Parameter(description = "Contenido del archivo .torrent codificado en base64. Debe ser un archivo .torrent válido.", required = true, example = "ZGVtbzEyMzQ1Njc4OTA=") @RequestBody String torrentFile) {
        transmissionService.addTorrent(torrentFile);
        return ResponseEntity.ok("Torrent añadido correctamente");
    }

    /**
     * Añade un torrent al cliente Transmission desde un enlace magnet.
     *
     * <p>Este endpoint permite añadir un nuevo torrent a Transmission utilizando un enlace magnet.
     * Los enlaces magnet contienen toda la información necesaria para descargar el torrent sin
     * necesidad de un archivo .torrent físico.</p>
     *
     * @param request enlace magnet válido que contiene la información del torrent.
     *                  Debe comenzar con 'magnet:?' y contener los parámetros necesarios.
     * @return ResponseEntity con mensaje de confirmación si el magnet se añade correctamente
     * @throws IllegalArgumentException si el enlace magnet es inválido o nulo
     */
    @PostMapping("/magnet")
    @Operation(
        summary = "Añadir torrent desde magnet link",
        description = "Añade un nuevo torrent al cliente Transmission desde un enlace magnet"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Magnet link añadido correctamente al cliente Transmission"),
            @ApiResponse(responseCode = "400", description = "El enlace magnet proporcionado es inválido o malformado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud")
    })
    public ResponseEntity<String> addMagnet(
           @RequestBody TransmissionMagnetRequest request) {
        transmissionService.addMagnet(request.getMagnet());
        return ResponseEntity.ok("Magnet añadido correctamente");
    }

    /**
     * Recupera la lista completa de torrents del cliente Transmission ordenados por estado.
     *
     * <p>Este endpoint devuelve todos los torrents actualmente gestionados por Transmission,
     * ordenados alfabéticamente por su estado (downloading, seeding, paused, etc.).
     * La respuesta incluye tanto el conteo total de torrents como la lista completa.</p>
     *
     * @return TransmissionRestResponse que contiene el número total de torrents y la lista
     *         ordenada de objetos Torrent con toda su información (ID, nombre, estado,
     *         progreso, tamaños, velocidades de descarga/subida)
     */
    @GetMapping("/torrents")
    @Operation(
        summary = "Listar torrents ordenados por estado",
        description = "Recupera la lista completa de torrents del cliente Transmission, ordenados alfabéticamente por estado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de torrents recuperada y ordenada correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al recuperar la lista de torrents")
    })
    public TransmissionRestResponse listTorrents() {
        List<Torrent> lista = transmissionService.listTorrents();
        lista.sort(Comparator.comparing(Torrent::status));
        return new TransmissionRestResponse(lista.size(), lista);
    }

    /**
     * Recupera torrents desde un sitio de búsqueda preconfigurado y los añade automáticamente a Transmission.
     *
     * <p>Este endpoint busca torrents en 1337x con categoría 72, subcategoría 1, máximo 10 resultados,
     * y añade automáticamente los enlaces magnet de los resultados a Transmission.</p>
     *
     * @return ResponseEntity con mensaje indicando cuántos torrents se añadieron
     */
    @PostMapping("/add-torrents")
    @Operation(
        summary = "Buscar y añadir torrents automáticamente",
        description = "Recupera torrents desde un sitio de búsqueda preconfigurado y los añade automáticamente a Transmission"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torrents añadidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al buscar o añadir torrents")
    })
    public ResponseEntity<String> addTorrentsFromSearch() {

        try {
            // Valores preconfigurados
            String site = "1337x";
            String category = "72";
            int maxResults = 10;

            // Buscar torrents
            List<TorrentSearchResult> results = torrentSearchService.searchTorrents(site, category, null, maxResults);

            int addedCount = 0;
            for (TorrentSearchResult result : results) {
                try {
                    if (result.getDownload_link() == null || result.getDownload_link().isEmpty()) {
                        continue; // Saltar si no hay enlace de descarga
                    }
                    transmissionService.addMagnet(result.getDownload_link());
                    addedCount++;
                } catch (Exception e) {
                    // Log error but continue with others
                    // You might want to add logging here
                }
            }

            return ResponseEntity.ok("Se añadieron " + addedCount + " de " + results.size() + " torrents encontrados.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar o añadir torrents: " + e.getMessage());
        }
    }
}
