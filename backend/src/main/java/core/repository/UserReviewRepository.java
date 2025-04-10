package core.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.model.UserReview;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview, String> {
	List<UserReview> findByUserId(String userId);
}
