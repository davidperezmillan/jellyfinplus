package com.davidperezmillan.jellyfinplus.infrastructure.adapters.web.mobile;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SearchController {

    @GetMapping("/series/{title}")
    public String searchSeries(@PathVariable String title) {
        // Lógica de búsqueda para series aquí (placeholder)
        return "Searching for series with title: " + title;
    }

    @GetMapping("/movies/{title}")
    public String searchMovies(@PathVariable String title) {
        // Lógica de búsqueda para películas aquí (placeholder)
        return "Searching for movie with title: " + title;
    }

    @GetMapping("/others/{title}")
    public String searchOthers(@PathVariable String title) {
        // Lógica de búsqueda para otros aquí (placeholder)
        return "Searching for other media with title: " + title;
    }
}
