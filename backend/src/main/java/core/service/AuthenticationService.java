package core.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import core.model.UserCredential;
import core.model.UserLogin;
import core.repository.UserCredentialRepository;
import core.repository.UserLoginRepository;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserLoginRepository userLoginRepository;
	private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserCredentialRepository userCredentialRepository, 
    		UserLoginRepository userLoginRepository,
    		PasswordEncoder passwordEncoder) {
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.userLoginRepository = userLoginRepository;
    }
    
    public boolean validateLogin(String email, String rawPassword) {
        return userCredentialRepository.findByEmail(email)
                .map(uc -> passwordEncoder.matches(rawPassword, uc.getPassword()))
                .orElse(false);
    }
    
    public void login(UserLogin userLogin) {
    	userLoginRepository.save(userLogin);
    }
    
    @Transactional
    public void logout(String token) {
    	userLoginRepository.deleteById(token);
    }
    
    public UserLogin getUserLogin(String token) {
    	UserLogin userLogin = userLoginRepository.findById(token)
				.orElseThrow(() -> new IllegalArgumentException("UserLogin does not exist"));
		return userLogin;
    }
    
	public UserCredential getUserCredentialByEmail(String email) {
		UserCredential userCredential = userCredentialRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User with email " + email + " does not exist"));
		return userCredential;
	}
}