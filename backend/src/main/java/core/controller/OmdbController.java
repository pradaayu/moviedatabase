package core.controller;

import core.service.OmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/omdb")
public class OmdbController {

    @Autowired
    private OmdbService omdbService;

    /**
     * Search endpoint that returns multiple movies matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchMovies(
            @RequestParam String s,  // Using 's' to match the OMDB API parameter
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String y,  // Using 'y' to match the OMDB API parameter
            @RequestParam(required = false, defaultValue = "1") Integer page) {

        return ResponseEntity.ok(omdbService.searchMovies(s, type, y, page));
    }

    /**
     * Get movie details by IMDB ID
     */
    @GetMapping("/id/{imdbId}")
    public ResponseEntity<String> getMovieById(
            @PathVariable String imdbId,
            @RequestParam(required = false, defaultValue = "short") String plot) {

        return ResponseEntity.ok(omdbService.getMovieById(imdbId, plot));
    }

    /**
     * Get movie by exact title match
     */
    @GetMapping("/title")
    public ResponseEntity<String> getMovieByTitle(
            @RequestParam String t,  // Using 't' to match the OMDB API parameter
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String y,  // Using 'y' to match the OMDB API parameter
            @RequestParam(required = false, defaultValue = "short") String plot) {

        return ResponseEntity.ok(omdbService.getMovieByTitle(t, type, y, plot));
    }
}