package controller;

import jakarta.servlet.http.HttpServletRequest;
import model.User;
import model.UserReview;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthenticationService;
import service.UserReviewService;
import utils.JwtUtil;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reviews")
public class UserReviewController {
    private final UserReviewService userReviewService;
    private final AuthenticationService authService;
    private final JwtUtil jwtUtil;

    public UserReviewController(UserReviewService userReviewService, AuthenticationService authService, JwtUtil jwtUtil) {
        this.userReviewService = userReviewService;
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

//    @GetMapping
//    public ResponseEntity<List<UserReview>> getUserReviews(HttpServletRequest request) {
//        String token = jwtUtil.extractTokenFromCookie(request);
//        if (token == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//
//        String email = jwtUtil.extractEmail(token);
//        User user = authService.getUserLoginByEmail(email).getUser();
//        return ResponseEntity.ok(user.getUserReviews());
//    }
//    
//    @PutMapping("/my")
//    public ResponseEntity<?> updateUserReview(@RequestBody UserReview userReview, HttpServletRequest request) {
//        String token = jwtUtil.extractTokenFromCookie(request);
//        if (token == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//        
//        String userId = authService.getUserLoginByEmail(jwtUtil.extractEmail(token)).getUser().getId();
//        // Ensure that the review belongs to the authenticated user
//        if (!Objects.equals(userReview.getUser().getId(), userId)) {
//        	return ResponseEntity.status(401).build(); // Unauthorized
//        }
//        
//        try {
//        	userReviewService.updateReview(userReview);
//        	return ResponseEntity.ok("User review updated successfully");
//        } catch (Exception e) {
//        	return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    
    @GetMapping
    public ResponseEntity<List<UserReview>> getAllReviews() {
        List<UserReview> reviews = userReviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }
}
