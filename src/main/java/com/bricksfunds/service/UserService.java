package com.bricksfunds.service;

import java.util.List;
import java.util.Set;

import com.bricksfunds.entity.User;
import com.bricksfunds.entity.UserRole;


public interface UserService {

	
	//creating user
	
	public User createUser(User user, Set<UserRole> userRoles) throws Exception;


	//getUser
	public User getUser(String username);
	
	public List<User> getUserByMail(String mail);
	
	public User findByYourReferCode(String referCode);
	
	public List<User> findyByFriendRefer(String yourReferCode,boolean enable);
	
	public List<User> findAllUser();
	
	public User findById(Long id);
	
	public User findByUsernameIgonreCase(String username);
	
	//delete User
	public void deleteUser(Long userId);


}
