package core.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.model.User;
import core.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Transactional
	public void deleteUser(String userId) {
	    userRepository.deleteById(userId);;
	}
	
	public void updateUser(User user) {
		userRepository.save(user);
	}
	
    @Transactional
    public void createUser(User user) {
        userRepository.save(user);
    }
    
    public List<User> getAll() {
    	return userRepository.findAll();
    }
}
