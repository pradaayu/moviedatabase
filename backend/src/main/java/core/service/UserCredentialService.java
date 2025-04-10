package core.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import core.model.UserCredential;
import core.repository.UserCredentialRepository;

@Service
public class UserCredentialService {
	private final UserCredentialRepository userCredentialRepository;
	
	public UserCredentialService(UserCredentialRepository userCredentialRepository) {
		this.userCredentialRepository = userCredentialRepository;
	}
	
	public void registerUser(UserCredential userCredential) {
		userCredentialRepository.save(userCredential);
	}
	
	public Optional<UserCredential> findByEmail(String email) {
		return userCredentialRepository.findByEmail(email);
	}
	
	
	
}
