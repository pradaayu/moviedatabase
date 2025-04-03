package controller;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import model.User;
import model.UserCredential;
import service.AuthenticationService;
import utils.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Registers a new user in the system. Saves user's provided data in DB.
     * @param request the RegisterRequest object containing the user's registration details
     * @return a ResponseEntity containing a success message and an HTTP status of 201 (Created) 
     * if the user is registered successfully.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setDateOfBirth(request.getDateOfBirth());

        authenticationService.createUser(user, request.getPassword(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    
    /**
     * Issues accessToken in the response body. Stores refreshToken as an HTTP-only cookie.
     * @param request the LoginRequest object containing the user's login details to authenticate the user.
     * @param response the HttpServletResponse object used to craft the HTTP response 
     * (e.g., setting headers, status codes, or writing the response body).
     * @return a ResponseEntity containing access token (String)
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        boolean isValid = authenticationService.validateLogin(request.getEmail(), request.getPassword());
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        
        UserCredential userCredential = authenticationService.getUserCredentialByEmail(request.getEmail());
        String userId = userCredential.getUser().getId();
        String accessToken = jwtUtil.generateAccessToken(request.getEmail(), userId);
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail(), userId);
        
        // Store refresh token in an HTTP-only cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/auth/refresh"); // Accessible only by refresh endpoint
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(accessToken);
    }

    /**
     * 
     * @param refreshToken sent automatically via cookies
     * @return a ResponseEntity containing a new access token (String)
     */
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        String userId = jwtUtil.extractUserId(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(email, userId);
        return ResponseEntity.ok(newAccessToken);
    }

    /**
     * Deletes the refreshToken cookie.
     * @param response the HttpServletResponse object used to craft the HTTP response 
     * (e.g., setting headers, status codes, or writing the response body).
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
    	// Remove refresh token by making the cookie expire
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/auth/refresh");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
        return ResponseEntity.ok("Logged out successfully");
    }
    
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private Date dateOfBirth;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public Date getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
