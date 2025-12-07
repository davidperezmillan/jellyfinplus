package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.ports.SeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeriesService {

    private static final Logger log = LoggerFactory.getLogger(SeriesService.class);

    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
        log.info("SeriesService initialized");
    }

    public List<Series> getAllSeries() {
        log.info("Getting all series");
        List<Series> series = seriesRepository.findAll();
        log.info("Retrieved {} series", series.size());
        return series;
    }

    public List<Series> getDownloadedSeries() {
        log.info("Getting downloaded series");
        List<Series> series = seriesRepository.findDownloaded();
        log.info("Retrieved {} downloaded series", series.size());
        return series;
    }
}
