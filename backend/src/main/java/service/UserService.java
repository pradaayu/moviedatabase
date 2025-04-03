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
	    userRepository.deleteById(userId);;
	}
	
	public void updateUser(User user) {
		userRepository.save(user);
	}
}
