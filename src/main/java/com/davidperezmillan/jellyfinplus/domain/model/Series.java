package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una serie de televisión en Jellyfin")
public record Series(
    @Schema(description = "Identificador único de la serie", example = "12345")
    String id,
    @Schema(description = "Nombre de la serie", example = "Breaking Bad")
    String name,
    @Schema(description = "Descripción o sinopsis de la serie", example = "Una serie sobre un profesor de química...")
    String overview,
    @Schema(description = "Indica si la serie está descargada localmente", example = "true")
    boolean isDownloaded
) {
}
