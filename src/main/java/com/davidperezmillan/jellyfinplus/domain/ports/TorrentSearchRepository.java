package com.davidperezmillan.jellyfinplus.domain.ports;

import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentSearchResult;
import java.util.List;

public interface TorrentSearchRepository {
    List<TorrentSearchResult> searchTorrents(String site, String category, String page, int maxResults);
}
