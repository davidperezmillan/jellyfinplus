package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.ports.SeriesRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public List<Series> getAllSeries() {
        return seriesRepository.findAll();
    }

    public List<Series> getDownloadedSeries() {
        return seriesRepository.findDownloaded();
    }
}
