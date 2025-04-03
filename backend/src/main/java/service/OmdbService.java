package service;

import config.OmdbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OmdbService {

    private static final String OMDB_API_BASE_URL = "http://www.omdbapi.com/";

    @Autowired
    private OmdbConfig apiConfig;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Search for movies by title
     * Uses the 's' parameter in OMDB API
     */
    // @Cacheable("movieSearchResults")
    public String searchMovies(String title, String type, String year, Integer page) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(OMDB_API_BASE_URL)
            .queryParam("apikey", apiConfig.getOmdbApiKey())
            .queryParam("s", title);

        if (type != null && !type.isEmpty()) {
            builder.queryParam("type", type);
        }

        if (year != null && !year.isEmpty()) {
            builder.queryParam("y", year);
        }

        if (page != null && page > 0) {
            builder.queryParam("page", page);
        }

        return restTemplate.getForObject(builder.toUriString(), String.class);
    }

    /**
     * Get detailed information about a movie by its IMDB ID
     * Uses the 'i' parameter in OMDB API
     */
    // @Cacheable("movieDetails")
    public String getMovieById(String imdbId, String plot) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(OMDB_API_BASE_URL)
            .queryParam("apikey", apiConfig.getOmdbApiKey())
            .queryParam("i", imdbId);

        if (plot != null && !plot.isEmpty()) {
            builder.queryParam("plot", plot);
        }

        return restTemplate.getForObject(builder.toUriString(), String.class);
    }

    /**
     * Get movie by title (exact match)
     * Uses the 't' parameter in OMDB API
     */
    // @Cacheable("movieByTitle")
    public String getMovieByTitle(String title, String type, String year, String plot) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl(OMDB_API_BASE_URL)
            .queryParam("apikey", apiConfig.getOmdbApiKey())
            .queryParam("t", title);

        if (type != null && !type.isEmpty()) {
            builder.queryParam("type", type);
        }

        if (year != null && !year.isEmpty()) {
            builder.queryParam("y", year);
        }

        if (plot != null && !plot.isEmpty()) {
            builder.queryParam("plot", plot);
        }

        return restTemplate.getForObject(builder.toUriString(), String.class);
    }
}
