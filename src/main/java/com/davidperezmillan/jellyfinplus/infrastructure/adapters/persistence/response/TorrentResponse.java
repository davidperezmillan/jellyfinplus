package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TorrentResponse {

    private int id;
    private String name;
    private int status;
    @JsonProperty("percentDone")
    private double percentDone;
    @JsonProperty("totalSize")
    private long totalSize;
    @JsonProperty("rateDownload")
    private long rateDownload;
    @JsonProperty("rateUpload")
    private long rateUpload;

}
