package controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import model.User;
import model.UserMovie;
import service.AuthenticationService;
import service.UserMovieService;
import utils.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserMovieController {
    private final UserMovieService userMovieService;
    
    private final JwtUtil jwtUtil;

    public UserMovieController(
    		UserMovieService userMovieService, AuthenticationService authService, JwtUtil jwtUtil) {
        this.userMovieService = userMovieService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<UserMovie>> getUserMovies(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromCookie(request);
        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized if no token
        }
        String userId = jwtUtil.extractUserId(token);
        List<UserMovie> movies = userMovieService.getUserMovies(userId);
        return ResponseEntity.ok(movies);
    }
}
