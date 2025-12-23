package com.davidperezmillan.jellyfinplus.domain.ports;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import java.util.List;

public interface TransmissionRepository {
    void addTorrent(String torrentFile);
    void addMagnet(String magnetLink);
    List<Torrent> listTorrents();
}
