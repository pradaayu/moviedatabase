package core.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.model.User;
import core.model.UserMovie;
import core.service.UserMovieService;
import jakarta.servlet.http.HttpServletRequest;
import core.service.AuthenticationService;
import core.security.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserMovieController {
    private final UserMovieService userMovieService;
    private final AuthenticationService authService;
    private final JwtUtil jwtUtil;

    public UserMovieController(
    		UserMovieService userMovieService, AuthenticationService authService,
    		JwtUtil jwtUtil) {
        this.userMovieService = userMovieService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<UserMovie>> getUserMovies(HttpServletRequest request) {
//        String token = jwtUtil.extractTokenFromCookie(request);
//        if (token == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized if no token
//        }
//        String userId = jwtUtil.extractUserId(token);
//        List<UserMovie> movies = userMovieService.getUserMovies(userId);
//        return ResponseEntity.ok(movies);
    	return ResponseEntity.ok(new ArrayList());
    }
}
