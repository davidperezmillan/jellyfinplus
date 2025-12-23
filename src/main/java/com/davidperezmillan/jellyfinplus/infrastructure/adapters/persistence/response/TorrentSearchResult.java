package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response;

import lombok.Data;

@Data
public class TorrentSearchResult {
    private String title;
    private String torrent_url;
    private String download_link;
    private String size;
    private String seeds;
    private String leeches;
    private String date;
    private String uploader;
    private String source;
}
