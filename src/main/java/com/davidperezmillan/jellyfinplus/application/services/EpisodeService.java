package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.ports.EpisodeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Comparator;

@Service
public class EpisodeService {

    private final EpisodeRepository episodeRepository;

    public EpisodeService(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    public List<Episode> getEpisodesBySeries(String seriesId) {
        return episodeRepository.findBySeriesId(seriesId);
    }

    public List<Episode> getUnwatchedEpisodesBySeries(String seriesId) {
        return getEpisodesBySeries(seriesId).stream()
                .filter(episode -> !episode.isPlayed())
                .toList();
    }

    public Episode getNextEpisode(String seriesId) {
        List<Episode> episodes = getEpisodesBySeries(seriesId);
        // Filtrar episodios que no sean especiales (asumiendo seasonNumber > 0)
        List<Episode> regularEpisodes = episodes.stream()
                .filter(e -> e.seasonNumber() > 0)
                .toList();
        if (regularEpisodes.isEmpty()) {
            return null; // O un episodio por defecto
        }
        // Encontrar la temporada con el mayor número
        int maxSeason = regularEpisodes.stream()
                .mapToInt(Episode::seasonNumber)
                .max()
                .orElse(0);
        // Filtrar episodios de la última temporada
        List<Episode> lastSeasonEpisodes = regularEpisodes.stream()
                .filter(e -> e.seasonNumber() == maxSeason)
                .toList();
        // Encontrar el episodio con el mayor episodeNumber en la última temporada
        Episode lastEpisode = lastSeasonEpisodes.stream()
                .max(Comparator.comparingInt(Episode::episodeNumber))
                .orElse(null);
        if (lastEpisode == null) {
            return null;
        }
        // Crear el siguiente episodio: episodeNumber +1, mismo seasonNumber, otros campos null o por defecto
        return new Episode(
                null, // id
                "Próximo Episodio", // name
                "Episodio siguiente a conseguir", // overview
                seriesId,
                lastEpisode.seasonNumber(),
                lastEpisode.episodeNumber() + 1,
                false, // isDownloaded
                false  // isPlayed
        );
    }
}
