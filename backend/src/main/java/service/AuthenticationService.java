package service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.User;
import model.UserCredential;
import repository.UserCredentialRepository;
import repository.UserRepository;
import utils.HybridClockUUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
    	UserRepository userRepository, 
    	UserCredentialRepository userCredentialRepository,
    	PasswordEncoder passwordEncoder
		) {
        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(User user, String password, String email) {
        // Check if user with this email already exists
        if (userCredentialRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with email " + email + " is already registered!");
        }
        // Set user ID if not already set
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId("u-" + HybridClockUUID.generate());
        }
//        userRepository.save(user);

        // Create and save user login info
        UserCredential userCredential = new UserCredential();
        userCredential.setEmail(email);
        userCredential.setPassword(passwordEncoder.encode(password)); // Encoding handles salting automatically
        // No need to set salt separately
        // The salt is automatically generated and embedded within the resulting hash
//        userLogin.setUser(user);
//        userLoginRepository.save(userLogin);
        user.setUserCredential(userCredential);
        userRepository.save(user);
    }
    
    public boolean validateLogin(String email, String rawPassword) {
        return userCredentialRepository.findByEmail(email)
                .map(userLogin -> passwordEncoder.matches(rawPassword, userLogin.getPassword()))
                .orElse(false);
    }
    
	public UserCredential getUserCredentialByEmail(String email) {
		UserCredential userCredential = userCredentialRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User with email " + email + " does not exist"));
		return userCredential;
	}
}