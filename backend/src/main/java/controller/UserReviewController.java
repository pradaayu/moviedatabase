package controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.UserReview;
import service.UserReviewService;

public class UserReviewController {
	@RestController
	@RequestMapping("/api/user")
	public class UserMovieController {
	    private final UserReviewService userReviewService;

	    public UserMovieController(UserReviewService userReviewService) {
	        this.userReviewService = userReviewService;
	    }

	    @GetMapping("/{userId}/reviews")
	    public List<UserReview> getUserReviews(@PathVariable String userId) {
	        return userReviewService.getUserReviews(userId);
	    }
	}
}
