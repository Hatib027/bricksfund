package com.bricksfunds.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.ForgotPassword;
import com.bricksfunds.entity.User;
import com.bricksfunds.entity.UserRole;
import com.bricksfunds.repo.ForgotPasswordRepository;
import com.bricksfunds.repo.RoleRepository;
import com.bricksfunds.repo.UserRepository;




@Service
public class UserServiceImpl  implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	ForgotPasswordRepository forgotPasswordRepository;
	
	
	//creating user
	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		
		User local = this.userRepository.findByUsername(user.getUsername());
		
//		if(local != null) {
//			System.out.println("User is Already There !!");
//			throw new Exception("User Already Present !!");
//		}else {
//			
////			for(UserRole ur:userRoles) {
////				roleRepository.save(ur.getRole());
////			}
//			
//			user.getUserRoles().addAll(userRoles);
//			
//			local = this.userRepository.save(user);
//			
//		}
		
		if(local == null) {
			for(UserRole ur:userRoles) {
				roleRepository.save(ur.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);	
			local = this.userRepository.save(user);
		
		}else {
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



	@Override
	public List<User> getUserByMail(String mail) {
		// TODO Auto-generated method stub
		return this.userRepository.findByEmail(mail);
	}



	@Override
	public User findByYourReferCode(String referCode) {
		// TODO Auto-generated method stub
		return this.userRepository.findByYourrefercode(referCode);
	}



	@Override
	public List<User> findyByFriendRefer(String yourReferCode,boolean enable) {
		// TODO Auto-generated method stub
		return this.userRepository.findByReferralcodeAndEnable(yourReferCode,enable);
	}



	@Override
	public List<User> findAllUser() {
		// TODO Auto-generated method stub
		return this.userRepository.findAll();
	}



	@Override
	public User findById(Long id) {
		// TODO Auto-generated method stub
		return this.userRepository.findById(id).get();
	}



	@Override
	public User findByUsernameIgonreCase(String username) {
		// TODO Auto-generated method stub
		return this.userRepository.findByUsernameIgnoreCase(username);
	}
	
	public void addForgotData(ForgotPassword f) {
		
		
		this.forgotPasswordRepository.save(f);
	}
	
	public ForgotPassword findByEmailForgot(String email) {
		return this.forgotPasswordRepository.findByEmail(email);
	}
	
	public ForgotPassword findByTokenForgot(String token) {
		return this.forgotPasswordRepository.findByToken(token);
	}

}
