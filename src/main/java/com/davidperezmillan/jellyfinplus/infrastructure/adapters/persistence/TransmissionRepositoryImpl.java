package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.domain.ports.TransmissionRepository;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.external.TransmissionApiClient;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TransmissionRepositoryImpl implements TransmissionRepository {

    private final TransmissionApiClient apiClient;

    public TransmissionRepositoryImpl(TransmissionApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public void addTorrent(String torrentFile) {
        apiClient.addTorrent(torrentFile);
    }

    @Override
    public void addMagnet(String magnetLink) {
        apiClient.addMagnet(magnetLink);
    }

    @Override
    public List<Torrent> listTorrents() {
        return apiClient.listTorrents();
    }
}
