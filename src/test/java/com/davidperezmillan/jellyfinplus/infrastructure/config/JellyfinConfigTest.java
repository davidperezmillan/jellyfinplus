package com.davidperezmillan.jellyfinplus.infrastructure.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JellyfinConfigTest {

    @Test
    void gettersAndSetters_work() {
        JellyfinConfig cfg = new JellyfinConfig();
        assertThat(cfg.getBaseUrl()).isEqualTo("http://localhost:8096");

        cfg.setBaseUrl("http://example");
        cfg.setToken("t");
        cfg.setUserId("u");
        cfg.setUserName("n");

        assertThat(cfg.getBaseUrl()).isEqualTo("http://example");
        assertThat(cfg.getToken()).isEqualTo("t");
        assertThat(cfg.getUserId()).isEqualTo("u");
        assertThat(cfg.getUserName()).isEqualTo("n");
    }
}

