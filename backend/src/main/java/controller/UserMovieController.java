package controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.UserMovie;
import service.UserMovieService;

@RestController
@RequestMapping("/api/user")
public class UserMovieController {
    private final UserMovieService userMovieService;

    public UserMovieController(UserMovieService userMovieService) {
        this.userMovieService = userMovieService;
    }

    @GetMapping("/{userId}/movies")
    public List<UserMovie> getUserMovies(@PathVariable String userId) {
        return userMovieService.getUserMovies(userId);
    }
}
