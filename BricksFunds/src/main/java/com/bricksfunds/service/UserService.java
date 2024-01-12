package com.bricksfunds.service;

import java.util.Set;

import com.bricksfunds.entity.User;
import com.bricksfunds.entity.UserRole;


public interface UserService {

	
	//creating user
	
	public User createUser(User user, Set<UserRole> userRoles) throws Exception;


	//getUser
	public User getUser(String username);
	
	//delete User
	public void deleteUser(Long userId);


}
