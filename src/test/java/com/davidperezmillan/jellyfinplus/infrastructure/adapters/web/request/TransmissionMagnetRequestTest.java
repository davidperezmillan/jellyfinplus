package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransmissionMagnetRequestTest {

    @Test
    void shouldCreateTransmissionMagnetRequestWithMagnet() {
        // Given
        String magnet = "magnet:?xt=urn:btih:1234567890";

        // When
        TransmissionMagnetRequest request = new TransmissionMagnetRequest();
        request.setMagnet(magnet);

        // Then
        assertThat(request.getMagnet()).isEqualTo(magnet);
    }
}
