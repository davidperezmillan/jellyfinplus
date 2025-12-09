package com.davidperezmillan.jellyfinplus.infrastructure.adapters.external;

import com.davidperezmillan.jellyfinplus.domain.model.Episode;
import com.davidperezmillan.jellyfinplus.domain.model.Series;
import com.davidperezmillan.jellyfinplus.infrastructure.config.JellyfinConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JellyfinApiClientTest {

    private JellyfinConfig config;
    private JellyfinApiClient client;
    private WebClient webClient;
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() throws Exception {
        config = new JellyfinConfig();
        config.setBaseUrl("http://localhost:8096");
        config.setToken("token");
        client = new JellyfinApiClient(config);

        // Mock WebClient and its chain
        webClient = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        // Inject mock WebClient into the private final field
        Field webClientField = JellyfinApiClient.class.getDeclaredField("webClient");
        webClientField.setAccessible(true);
        webClientField.set(client, webClient);
    }

    @Test
    void getSeries_withUserIdConfigured_parsesSeries() {
        config.setUserId("u1");
        String seriesJson = "{\"Items\":[{\"Id\":\"s1\",\"Name\":\"Series 1\",\"CommunityRating\":8.5,\"PremiereDate\":\"2020-01-01\",\"Status\":\"Continuing\"}]}";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(seriesJson));

        List<Series> result = client.getSeries();

        assertThat(result).hasSize(1);
        Series s = result.get(0);
        assertThat(s.id()).isEqualTo("s1");
        assertThat(s.name()).isEqualTo("Series 1");
        assertThat(s.communityRating()).isEqualTo(8.5);
        assertThat(s.premiereDate()).isEqualTo("2020-01-01");
        assertThat(s.status()).isEqualTo("Continuing");
    }

    @Test
    void getUserId_withUserNameConfigured_findsUserByName() {
        config.setUserName("tester");
        String usersJson = "[{\"Id\":\"uX\",\"Name\":\"tester\"}]";
        String seriesJson = "{\"Items\":[]}";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just(usersJson))
                .thenReturn(Mono.just(seriesJson));

        List<Series> result = client.getSeries();
        assertThat(result).isEmpty();
    }

    @Test
    void getUserId_whenUserNotFound_throws() {
        config.setUserName("nope");
        String usersJson = "[{\"Id\":\"u1\",\"Name\":\"other\"}]";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(usersJson));

        assertThatThrownBy(() -> client.getSeries())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error getting user id");
    }

    @Test
    void getEpisodes_parsesDownloadedAndPlayedFlags() {
        config.setUserId("u1");
        String episodesJson = "{\"Items\":[{\"Id\":\"e1\",\"Name\":\"E1\",\"Overview\":\"ov\",\"ParentIndexNumber\":1,\"IndexNumber\":1,\"Path\":\"/path/file.mkv\",\"UserData\":{\"Played\":true}}]}";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(episodesJson));

        List<Episode> eps = client.getEpisodes("s1");
        assertThat(eps).hasSize(1);
        Episode e = eps.get(0);
        assertThat(e.id()).isEqualTo("e1");
        assertThat(e.isDownloaded()).isTrue();
        assertThat(e.isPlayed()).isTrue();
        assertThat(e.seriesId()).isEqualTo("s1");
        assertThat(e.seasonNumber()).isEqualTo(1);
        assertThat(e.episodeNumber()).isEqualTo(1);
    }

    @Test
    void getAllEpisodes_parsesSeriesIdAndFlags() {
        config.setUserId("u1");
        String allEpisodesJson = "{\"Items\":[{\"Id\":\"a1\",\"Name\":\"A1\",\"SeriesId\":\"sX\",\"Path\":\"/p\",\"UserData\":{\"Played\":false}}]}";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(allEpisodesJson));

        List<Episode> eps = client.getAllEpisodes();
        assertThat(eps).hasSize(1);
        Episode e = eps.get(0);
        assertThat(e.id()).isEqualTo("a1");
        assertThat(e.seriesId()).isEqualTo("sX");
        assertThat(e.isDownloaded()).isTrue();
        assertThat(e.isPlayed()).isFalse();
    }

    @Test
    void getEpisodes_restThrows_wrappedInRuntimeException() {
        config.setUserId("u1");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("network")));

        assertThatThrownBy(() -> client.getEpisodes("s1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error parsing episodes");
    }
}
