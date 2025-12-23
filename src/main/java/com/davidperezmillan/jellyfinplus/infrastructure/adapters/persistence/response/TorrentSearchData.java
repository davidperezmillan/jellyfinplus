package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
public class TorrentSearchData {
    private List<TorrentSearchResult> torrents;
    private int count;
    private String url;
    private String source;
    private String category;
    private String page;
    private String status;
    private String site;
}
