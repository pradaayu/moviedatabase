package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import model.User;
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
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setDateOfBirth(request.getDateOfBirth());
        
        authenticationService.createUser(user, request.getPassword(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        boolean isValid = authenticationService.validateLogin(request.getEmail(), request.getPassword());

        if (isValid) {
            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
    
    // Request classes for data binding
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private java.time.LocalDateTime dateOfBirth;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public java.time.LocalDateTime getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(java.time.LocalDateTime dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    }
    
    public static class LoginRequest {
        private String email;
        private String password;
        
        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
