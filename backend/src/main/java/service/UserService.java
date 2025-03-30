package service;

import org.springframework.stereotype.Service;

import model.User;
import repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void deleteUser(String userId) {
	    User user = userRepository.findById(userId)
	    		.orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist"));
	    userRepository.delete(user);
	}
	
	public void updateUser(User user) {
		userRepository.save(user);
	}
}
