package core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import core.model.UserMovie;
import core.repository.UserMovieRepository;

@Service
public class UserMovieService {
    private final UserMovieRepository userMovieRepository;

    public UserMovieService(UserMovieRepository userMovieRepository) {
        this.userMovieRepository = userMovieRepository;
    }

    public void addMovie(UserMovie userMovie) {
        userMovieRepository.save(userMovie);
    }
    
    public void deleteMovie(UserMovie userMovie) {
    	userMovieRepository.delete(userMovie);
    }
    
    public List<UserMovie> getUserMovies(String userId) {
    	return userMovieRepository.findByUserId(userId);
    }
}
