package com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.mappers;

import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.infrastructure.adapters.persistence.response.TorrentResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransmissionMapper {

    public Torrent toDomain(TorrentResponse torrentResponse) {
        // Map status from int to string
        String status = mapStatus(torrentResponse.getStatus());

        return new Torrent(
                torrentResponse.getId(),
                torrentResponse.getName(),
                status,
                torrentResponse.getPercentDone(),
                torrentResponse.getTotalSize(),
                torrentResponse.getRateDownload(),
                torrentResponse.getRateUpload()
        );
    }

    public List<Torrent> toDomainList(List<TorrentResponse> torrentResponses) {
        return torrentResponses.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private String mapStatus(int statusCode) {
        // Transmission status codes: 0=stopped, 1=check pending, 2=checking, 3=download pending, 4=downloading, 5=seed pending, 6=seeding
        switch (statusCode) {
            case 0: return "stopped";
            case 1: return "check pending";
            case 2: return "checking";
            case 3: return "download pending";
            case 4: return "downloading";
            case 5: return "seed pending";
            case 6: return "seeding";
            default: return "unknown";
        }
    }

}
