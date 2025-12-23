package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.ports.TorrentSearchRepository;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.external.TorrentSearchApiClient;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request.TorrentSearchRequest;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TorrentSearchRepositoryImpl implements TorrentSearchRepository {

    private final TorrentSearchApiClient apiClient;

    public TorrentSearchRepositoryImpl(TorrentSearchApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<TorrentSearchResult> searchTorrents(String site, String category, String page, int maxResults) {
        TorrentSearchRequest request = new TorrentSearchRequest();
        request.setSite(site);
        TorrentSearchRequest.Criteria criteria = new TorrentSearchRequest.Criteria();
        criteria.setCategory(category);
        criteria.setPage(page);
        criteria.setMaxResults(maxResults);
        request.setCriteria(criteria);

        return apiClient.searchTorrents(request);
    }
}
