package core.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import core.model.UserCredential;
import core.repository.UserCredentialRepository;

@Service
public class AuthenticationService {
    private final UserCredentialRepository userCredentialRepository;
	private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserCredentialRepository userCredentialRepository, PasswordEncoder passwordEncoder) {
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
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