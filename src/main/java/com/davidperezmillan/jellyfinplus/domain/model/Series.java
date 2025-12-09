package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una serie de televisión en Jellyfin")
public record Series(
    @Schema(description = "Identificador único de la serie", example = "12345")
    String id,
    @Schema(description = "Nombre de la serie", example = "Breaking Bad")
    String name,
    @Schema(description = "Estado actual de la serie", example = "Continuing")
    String status,
    @Schema(description = "Calificación comunitaria de la serie", example = "8.5")
    double communityRating,

    @Schema(description = "Fecha de estreno de la serie", example = "2008-01-20")
    String premiereDate
) {
}
