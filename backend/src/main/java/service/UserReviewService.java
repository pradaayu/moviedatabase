package service;

import java.util.List;
import model.UserReview;
import repository.UserReviewRepository;

public class UserReviewService {
    private final UserReviewRepository userReviewRepository;

    public UserReviewService(UserReviewRepository userReviewRepository) {
        this.userReviewRepository = userReviewRepository;
    }

    public void addReview(UserReview userReview) {
        userReviewRepository.save(userReview);
    }
    
    public void deleteReview(UserReview userReview) {
    	userReviewRepository.delete(userReview);
    }

    public List<UserReview> getUserReviews(String userId) {
        return userReviewRepository.findByUserId(userId);
    }
}
