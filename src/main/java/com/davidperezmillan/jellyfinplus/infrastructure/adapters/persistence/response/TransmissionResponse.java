package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransmissionResponse {

    private String result;
    @JsonProperty("arguments")
    private ArgumentsResponse arguments;

}
