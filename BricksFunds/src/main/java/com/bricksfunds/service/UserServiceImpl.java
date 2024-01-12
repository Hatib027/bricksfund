package com.bricksfunds.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.User;
import com.bricksfunds.entity.UserRole;
import com.bricksfunds.repo.RoleRepository;
import com.bricksfunds.repo.UserRepository;




@Service
public class UserServiceImpl  implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	//creating user
	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		
		User local = this.userRepository.findByUsername(user.getUsername());
		
		if(local != null) {
			System.out.println("User is Already There !!");
			throw new Exception("User Already Present !!");
		}else {
			
//			for(UserRole ur:userRoles) {
//				roleRepository.save(ur.getRole());
//			}
			
			user.getUserRoles().addAll(userRoles);
			
			local = this.userRepository.save(user);
			
		}
		
		
		return local;
	}



	//getting user by username
	@Override
	public User getUser(String username) {
		
		return this.userRepository.findByUsername(username);
	}


	@Override
	public void deleteUser(Long userId) {
		this.userRepository.deleteById(userId);;
		
	}

}
