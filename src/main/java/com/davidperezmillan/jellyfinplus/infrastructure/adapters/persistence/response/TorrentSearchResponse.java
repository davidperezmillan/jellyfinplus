package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Data
public class TorrentSearchResponse {
    private String site;
    private String status;
    private TorrentSearchData data;
    private String timestamp;
}
