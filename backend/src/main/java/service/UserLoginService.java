package service;

import repository.UserLoginRepository;

public class UserLoginService {
	private final UserLoginRepository userLoginRepository;
	
	public UserLoginService(UserLoginRepository userLoginRepository) {
		this.userLoginRepository = userLoginRepository;
	}
}
