package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArgumentsResponse {

    @JsonProperty("torrent-added")
    private TorrentAdded torrentAdded;

    private List<TorrentResponse> torrents;

    @Data
    public static class TorrentAdded {
        private int id;
        private String name;
        @JsonProperty("hashString")
        private String hashString;
    }

}
