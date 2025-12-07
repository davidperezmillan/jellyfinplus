package com.davidperezmillan.jellyfinplus;

import com.davidperezmillan.jellyfinplus.application.services.SeriesService;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // If needed, but since .env is used, perhaps not
@TestPropertySource(properties = {
    "jellyfin.baseUrl=http://play.davidperezmillan.com/",
    "jellyfin.token=ee4bf04cad094a7b80d54adc8d7ae4ec",
    "jellyfin.userName=apps"
})
class JellyfinIntegrationTest {

    @Autowired
    private SeriesService seriesService;

    @Test
    void shouldRetrieveSeriesFromRealJellyfinInstance() {
        // This test connects to the real Jellyfin instance
        List<Series> series = seriesService.getAllSeries();

        // Basic assertions
        assertThat(series).isNotNull();
        assertThat(series).isNotEmpty(); // Assuming there are series

        // You can add more specific assertions based on your data
        Series firstSeries = series.get(0);
        assertThat(firstSeries.id()).isNotNull();
        assertThat(firstSeries.name()).isNotNull();
    }

    @Test
    void shouldRetrieveDownloadedSeriesFromRealJellyfinInstance() {
        List<Series> downloadedSeries = seriesService.getDownloadedSeries();

        assertThat(downloadedSeries).isNotNull();
        // Depending on your data, check if there are downloaded series
    }
}
