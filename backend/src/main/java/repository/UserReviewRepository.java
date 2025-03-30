package repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import model.UserReview;

public interface UserReviewRepository extends JpaRepository<UserReview, String> {
	List<UserReview> findByUserId(String userId);
}
