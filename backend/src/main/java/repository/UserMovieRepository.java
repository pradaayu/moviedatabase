package repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.UserMovie;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, String> {
	List<UserMovie> findByUserId(String userId);
}
