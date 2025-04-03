package service;

import java.util.List;

import org.springframework.stereotype.Service;

import model.UserReview;
import repository.UserReviewRepository;

@Service
public class UserReviewService {
    private final UserReviewRepository userReviewRepository;

    public UserReviewService(UserReviewRepository userReviewRepository) {
        this.userReviewRepository = userReviewRepository;
    }

    public void addReview(UserReview userReview) {
        userReviewRepository.save(userReview);
    }
    
    public void updateReview(UserReview userReview) {
        userReviewRepository.save(userReview);
    }
    
    public void deleteReview(UserReview userReview) {
    	userReviewRepository.delete(userReview);
    }
    
    public List<UserReview> getAllReviews() {
        return userReviewRepository.findAll();
    }
    
}
