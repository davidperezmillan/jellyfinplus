package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.ports.TorrentSearchRepository;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TorrentSearchService {

    private final TorrentSearchRepository torrentSearchRepository;

    public TorrentSearchService(TorrentSearchRepository torrentSearchRepository) {
        this.torrentSearchRepository = torrentSearchRepository;
    }

    public List<TorrentSearchResult> searchTorrents(String site, String category, String page, int maxResults) {
        return torrentSearchRepository.searchTorrents(site, category, page, maxResults);
    }
}
