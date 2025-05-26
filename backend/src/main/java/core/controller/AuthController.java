package core.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.model.User;
import core.model.UserCredential;
import core.model.UserLogin;
import core.service.UserCredentialService;
import core.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import core.service.AuthenticationService;
import utils.HybridClockUUID;
import core.security.JwtUtil;
import core.utils.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	// Auth requirements (coordinated with the front-end):
	// https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html
	
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserCredentialService userCredentialService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationService authenticationService, 
    		UserService userService, UserCredentialService userCredentialService,
    		PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userCredentialService = userCredentialService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Registers a new user in the system. Saves user's provided data in DB.
     * @param request the RegisterRequest object containing the user's registration details
     * @return a ResponseEntity containing a success message and an HTTP status of 201 (Created) 
     * if the user is registered successfully.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Object>> register(@Valid @RequestBody RegisterRequest request) {
		// Check if user with this email already exists
		if (userCredentialService.findByEmail(request.getEmail()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
				    .body(new ApiResponse<>(false, "Email already registered", null, "USER_EXISTS"));
		}
        
        User user = new User();
        user.setName(request.getName());
        user.setAvatar(request.getAvatar());
        String dob = request.getDateOfBirth();
        if (dob != null && !dob.isBlank()) {
        	user.setDateOfBirth(LocalDate.parse(dob));	
        }
        // Set user ID if not already set
        if (user.getId() == null || user.getId().isBlank()) {
            user.setId("u-" + HybridClockUUID.generate());
        }
        
        UserCredential userCredential = new UserCredential();
        userCredential.setEmail(request.getEmail());
        userCredential.setPassword(passwordEncoder.encode(request.getPassword()));
		userCredential.setUser(user);
		user.setUserCredential(userCredential);
		
        try {			
			userService.createUser(user);
			return ResponseEntity.status(HttpStatus.CREATED)
				    .body(new ApiResponse<>(true, "User registered successfully", null));
		} catch (Exception e) {
			e.printStackTrace();
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ApiResponse<>(false, "Internal server error", null));
		}
    }
    
    /**
     * Issues accessToken in the response body. Stores refreshToken as an HTTP-only cookie.
     * @param request the LoginRequest object containing the user's login details to authenticate the user.
     * @param response the HttpServletResponse object used to craft the HTTP response 
     * (e.g., setting headers, status codes, or writing the response body).
     * @return a ResponseEntity containing access token (String)
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        boolean isValid = authenticationService.validateLogin(request.getEmail(), request.getPassword());
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            		.body(new ApiResponse<>(false, "Invalid email or password", null));
        }
        
        UserCredential userCredential = authenticationService.getUserCredentialByEmail(request.getEmail());
        String userId = userCredential.getUser().getId();
        String accessToken = jwtUtil.generateAccessToken(request.getEmail(), userId);
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail(), userId);
        
        // Store refresh token in an HTTP-only cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/auth");
        refreshCookie.setMaxAge(jwtUtil.getRefreshExpirationTime());
        refreshCookie.setHttpOnly(true);
        response.addCookie(refreshCookie);
        
        // Create user login
        long t = System.currentTimeMillis();
        UserLogin userLogin = new UserLogin(refreshToken, userService.getUser(userId), t, t);
        authenticationService.login(userLogin);
        
    	return ResponseEntity.status(HttpStatus.ACCEPTED)
    			.body(new ApiResponse<>(true,"logged in",new AuthResponse(accessToken)));
    }

    /**
     * 
     * @param refreshToken sent automatically via cookies
     * @return a ResponseEntity containing a new access token (String)
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken, 
    		HttpServletResponse response) {
    	if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)	
            		.body(new ApiResponse<>(false, "No token", null));
    	}
    	
        // Check for no authorization or invalid token
    	if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)	
            		.body(new ApiResponse<>(false, "Invalid or expired refresh token", null));
        }
    	
        String email = jwtUtil.extractEmail(refreshToken);
        // Check if user exists
        if (!userCredentialService.findByEmail(email).isPresent()) {
        	// Generic consistent response to not reveal whether user exists or not
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        	        .body(new ApiResponse<>(false, "No valid user found", null));
        }
        
        // Get the corresponding active user login
        UserLogin userLogin = authenticationService.getUserLogin(refreshToken);
        if  (userLogin == null) {
        	// Remove refresh token by making the cookie expire
            Cookie refreshCookie = new Cookie("refreshToken", null);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/api/auth");
            refreshCookie.setMaxAge(0);
            response.addCookie(refreshCookie);
            
            // Remove the user login from DB
            authenticationService.logout(refreshToken);

        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        			.body(new ApiResponse<>(false,"No valid user login",null));
        }
    	// Generate new access token
        String userId = jwtUtil.extractUserId(refreshToken);
    	String newAccessToken = jwtUtil.generateAccessToken(email, userId);
    	// Update lastUsedAt
        userLogin.setLastUseTime(System.currentTimeMillis());
        authenticationService.login(userLogin);
        
    	return ResponseEntity.status(HttpStatus.ACCEPTED)
    			.body(new ApiResponse<>(true,null,new AuthResponse(newAccessToken)));
    }
    
    
	/**
	 * Records user activity by updating the last used time of an active session.
	 * Validates the refresh token and ensures there is an active user login.
	 * @param refreshToken the refresh token sent automatically via cookies
	 * @return a ResponseEntity containing a success message if activity is recorded successfully,
	 * or an error message with HTTP status 401 (Unauthorized) if the token is invalid or no active session exists
	 */
    @PostMapping("/ping")
    public ResponseEntity<ApiResponse<Object>> ping(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "Invalid or expired token", null));
        }
        
        UserLogin userLogin = authenticationService.getUserLogin(refreshToken);
        if (userLogin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, "No active session", null));
        }

        userLogin.setLastUseTime(System.currentTimeMillis());
        authenticationService.login(userLogin); // update user login in DB
        return ResponseEntity.ok(new ApiResponse<>(true, "Activity recorded", null));
    }


    /**
     * Deletes the refreshToken cookie.
     * @param response the HttpServletResponse object used to craft the HTTP response 
     * (e.g., setting headers, status codes, or writing the response body).
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(/*@RequestHeader("Authorization") String authorizationHeader,*/
    		@CookieValue(value = "refreshToken", required = false) String refreshToken,
    		HttpServletResponse response) {
//    	// To log out, one must be logged-in first
//    	if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//        }
//    	String accessToken = authorizationHeader.substring(7); // Remove "Bearer "
//    	if (!jwtUtil.validateToken(accessToken)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>(false, "Invalid access token", null));
//        }
    	
    	
        // Check for no authorization or invalid token
    	if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)	
            		.body(new ApiResponse<>(false, "Invalid or expired refresh token", null));
        }
    	
    	// Remove refresh token by making the cookie expire
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/api/auth");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);
       
        // Get the corresponding active user login
        UserLogin userLogin = authenticationService.getUserLogin(refreshToken);
        if  (userLogin == null) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        			.body(new ApiResponse<>(false,"Valid login does not exist",null));
        } else {
            // Remove the user login from DB
            authenticationService.logout(refreshToken);	
        }
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
        		.body(new ApiResponse<>(true, "Logged out successfully", null));
    }
    
    public static class RegisterRequest {
    	private final List<String> emojis = Arrays.asList("😎", "😄", "🤪", "🐱", "🦸", "🤢", "❤️", "😍", "👧", "💀");
    	
    	@NotBlank(message = "Name is required")
        private String name;
        
        @Email(message = "Invalid email format")
        private String email;
        
        @NotBlank(message = "Avatar is required")
        private String avatar;
        
        @Size(min = 4, max = 18, message = "Password must be between 4 and 18 characters long")
        private String password;
        
        private String dateOfBirth;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getAvatar() { return emojis.contains(avatar) ? avatar : ""; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    }

    public static class LoginRequest {
    	@NotBlank
        private String email;
    	@NotBlank
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class AuthResponse {
        private String accessToken;

        public AuthResponse(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAccessToken() { return accessToken; }
    }
    
}
