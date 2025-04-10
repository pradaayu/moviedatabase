package core.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import core.model.User;
import core.model.UserReview;
import core.service.UserReviewService;
import core.service.AuthenticationService;
import core.security.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/reviews")
public class UserReviewController {
    private final UserReviewService userReviewService;
    private final AuthenticationService authService;

    public UserReviewController(UserReviewService userReviewService, AuthenticationService authService) {
        this.userReviewService = userReviewService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<UserReview>> getUserReviews(HttpServletRequest request) {
//        String token = jwtUtil.extractTokenFromCookie(request);
//        if (token == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//
//        String email = jwtUtil.extractEmail(token);
//        User user = authService.getUserLoginByEmail(email).getUser();
        return ResponseEntity.ok(userReviewService.getAllReviews());
    }
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
   
}
