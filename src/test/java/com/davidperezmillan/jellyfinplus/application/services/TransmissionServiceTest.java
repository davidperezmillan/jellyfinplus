import com.davidperezmillan.jellyfinplus.application.services.TransmissionService;
import com.davidperezmillan.jellyfinplus.domain.model.Torrent;
import com.davidperezmillan.jellyfinplus.domain.ports.TransmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransmissionServiceTest {

    @Mock
    private TransmissionRepository transmissionRepository;

    private TransmissionService transmissionService;

    @BeforeEach
    void setUp() {
        transmissionService = new TransmissionService(transmissionRepository);
    }

    @Test
    void addTorrent_delegatesToRepository() {
        String torrentFile = "torrent data";
        transmissionService.addTorrent(torrentFile);
        verify(transmissionRepository).addTorrent(torrentFile);
    }

    @Test
    void addMagnet_delegatesToRepository() {
        String magnetLink = "magnet:?xt=urn:btih:...";
        transmissionService.addMagnet(magnetLink);
        verify(transmissionRepository).addMagnet(magnetLink);
    }

    @Test
    void listTorrents_delegatesToRepository() {
        List<Torrent> expected = List.of(new Torrent(1, "name", "status", 50.0, 1000L, 100L, 50L));
        when(transmissionRepository.listTorrents()).thenReturn(expected);
        List<Torrent> result = transmissionService.listTorrents();
        assert result.equals(expected);
        verify(transmissionRepository).listTorrents();
    }
}
