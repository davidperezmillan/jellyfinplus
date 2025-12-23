import com.davidperezmillan.jellyfinplus.infrastructure.config.TransmissionConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransmissionConfigTest {

    @Test
    void gettersAndSetters_work() {
        TransmissionConfig cfg = new TransmissionConfig();
        assertThat(cfg.getBaseUrl()).isEqualTo("http://localhost:9091/transmission/rpc");

        cfg.setBaseUrl("http://example");
        cfg.setUsername("user");
        cfg.setPassword("pass");
        cfg.setSessionId("sid");

        assertThat(cfg.getBaseUrl()).isEqualTo("http://example");
        assertThat(cfg.getUsername()).isEqualTo("user");
        assertThat(cfg.getPassword()).isEqualTo("pass");
        assertThat(cfg.getSessionId()).isEqualTo("sid");
    }
}
