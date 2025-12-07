package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un episodio de una serie en Jellyfin")
public record Episode(
    @Schema(description = "Identificador único del episodio", example = "67890")
    String id,
    @Schema(description = "Nombre del episodio", example = "Pilot")
    String name,
    @Schema(description = "Descripción del episodio", example = "El primer episodio de la serie...")
    String overview,
    @Schema(description = "ID de la serie a la que pertenece", example = "12345")
    String seriesId,
    @Schema(description = "Número de temporada", example = "1")
    int seasonNumber,
    @Schema(description = "Número de episodio", example = "1")
    int episodeNumber,
    @Schema(description = "Indica si el episodio está descargado localmente", example = "true")
    boolean isDownloaded,
    @Schema(description = "Indica si el episodio ha sido visto", example = "false")
    boolean isPlayed
) {
}
