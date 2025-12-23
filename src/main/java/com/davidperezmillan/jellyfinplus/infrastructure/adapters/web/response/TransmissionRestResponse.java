package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.response;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import lombok.Data;

import java.util.List;

@Data
public class TransmissionRestResponse {

    private int count;
    private List<Torrent> torrents;

    public TransmissionRestResponse(int size, List<Torrent> lista) {
        this.count = size;
        this.torrents = lista;
    }
}
