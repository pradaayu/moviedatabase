package service;

import java.util.List;
import org.springframework.stereotype.Service;
import model.UserMovie;
import repository.UserMovieRepository;

@Service
public class UserMovieService {
    private final UserMovieRepository userMovieRepository;

    public UserMovieService(UserMovieRepository userMovieRepository) {
        this.userMovieRepository = userMovieRepository;
    }

    public void addMovie(UserMovie userMovie) {
//        UserMovie userMovie = new UserMovie(user, movie);
        userMovieRepository.save(userMovie);
    }
    
    public void deleteMovie(UserMovie userMovie) {
//    	UserMovie userMovie = new UserMovie(user, movie);
    	userMovieRepository.delete(userMovie);
    }

    public List<UserMovie> getUserMovies(String userId) {
        return userMovieRepository.findByUserId(userId);
    }
}
