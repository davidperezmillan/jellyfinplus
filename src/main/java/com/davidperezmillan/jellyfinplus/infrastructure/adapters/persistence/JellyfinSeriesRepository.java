package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.domain.ports.SeriesRepository;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.external.JellyfinApiClient;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JellyfinSeriesRepository implements SeriesRepository {

    private final JellyfinApiClient apiClient;

    public JellyfinSeriesRepository(JellyfinApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<Series> findAll() {
        return apiClient.getSeries();
    }


}
