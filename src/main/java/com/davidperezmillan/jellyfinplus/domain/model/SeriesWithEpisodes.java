package com.davidperezmillan.jellyfinplus.domain.model;

import java.util.List;

/**
 * DTO que combina una entidad Series y su lista de Episodes para respuesta de endpoints.
 */
public record SeriesWithEpisodes(Series series, List<Episode> episodes) {
}

