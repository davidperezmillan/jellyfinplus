package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransmissionRequest {

    // "{\"method\": \"torrent-add\", \"arguments\": {\"filename\": \"%s\"}}", torrent.getMagnet());
    private String method;
    @JsonProperty("arguments")
    private ArgumentsRequest arguments;

}

