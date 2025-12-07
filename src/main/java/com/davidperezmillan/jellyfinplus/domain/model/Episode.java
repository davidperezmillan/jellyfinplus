package com.davidperezmillan.jellyfinplus.domain.model;

public record Episode(String id, String name, String overview, String seriesId, int seasonNumber, int episodeNumber, boolean isDownloaded) {
}
