package com.davidperezmillan.jellyfinplus.domain.ports;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import java.util.List;

public interface EpisodeRepository {
    List<Episode> findBySeriesId(String seriesId);
    List<Episode> findDownloaded();
}
