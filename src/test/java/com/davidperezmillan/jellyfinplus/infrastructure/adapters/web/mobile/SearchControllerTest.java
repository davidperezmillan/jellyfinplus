package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.mobile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchSeries_shouldReturnSearchResult() throws Exception {
        // Given
        String title = "Breaking Bad";

        // When & Then
        mockMvc.perform(get("/api/series/{title}", title))
                .andExpect(status().isOk())
                .andExpect(content().string("Searching for series with title: " + title));
    }

    @Test
    void searchMovies_shouldReturnSearchResult() throws Exception {
        // Given
        String title = "Inception";

        // When & Then
        mockMvc.perform(get("/api/movies/{title}", title))
                .andExpect(status().isOk())
                .andExpect(content().string("Searching for movie with title: " + title));
    }

    @Test
    void searchOthers_shouldReturnSearchResult() throws Exception {
        // Given
        String title = "Some Other Media";

        // When & Then
        mockMvc.perform(get("/api/others/{title}", title))
                .andExpect(status().isOk())
                .andExpect(content().string("Searching for other media with title: " + title));
    }
}
