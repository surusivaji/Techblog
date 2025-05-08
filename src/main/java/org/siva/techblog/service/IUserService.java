package org.siva.techblog.service;

import org.siva.techblog.model.User;
import org.springframework.data.domain.Page;

public interface IUserService {
	
	User saveUser(User user);
	
	User getUserByEmailAndPassword(String email, String password);
	
	Page<User> getAllUsers(int pageNo);
	
	User getUserById(int id);

	Boolean deleteUser(User user);
	
}
