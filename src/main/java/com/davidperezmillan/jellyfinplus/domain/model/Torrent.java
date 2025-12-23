package com.davidperezmillan.jellyfinplus.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "Representa un torrent en Transmission")
public record Torrent(
    @Schema(description = "Identificador único del torrent", example = "1")
    int id,
    @Schema(description = "Nombre del torrent", example = "Ubuntu ISO")
    String name,
    @Schema(description = "Estado del torrent", example = "downloading")
    String status,
    @Schema(description = "Progreso de descarga (0-100)", example = "50.5")
    double percentDone,
    @Schema(description = "Tamaño total en bytes", example = "1000000")
    long totalSize,
    @Schema(description = "Velocidad de descarga en bytes/segundo", example = "102400")
    long rateDownload,
    @Schema(description = "Velocidad de subida en bytes/segundo", example = "51200")
    long rateUpload
) {
}
