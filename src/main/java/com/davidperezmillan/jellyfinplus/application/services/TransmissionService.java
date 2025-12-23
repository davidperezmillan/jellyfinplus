package com.davidperezmillan.jellyfinplus.application.services;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.domain.ports.TransmissionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransmissionService {

    private final TransmissionRepository transmissionRepository;

    public TransmissionService(TransmissionRepository transmissionRepository) {
        this.transmissionRepository = transmissionRepository;
    }

    public void addTorrent(String torrentFile) {
        transmissionRepository.addTorrent(torrentFile);
    }

    public void addMagnet(String magnetLink) {
        transmissionRepository.addMagnet(magnetLink);
    }

    public List<Torrent> listTorrents() {
        return transmissionRepository.listTorrents();
    }
}
