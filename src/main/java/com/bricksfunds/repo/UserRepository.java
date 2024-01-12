package com.bricksfunds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bricksfunds.entity.User;



public interface UserRepository extends JpaRepository<User,Long>{
 
	public User findByUsernameAndEnable(String username,boolean enable);
	
	public User findByUsername(String username);
	
	public List<User> findByEmail(String email);
	
	public User findByYourrefercode(String refercode);
	
	public List<User> findByReferralcodeAndEnable(String yourRefferCode,boolean enable);
	
	
	
	public User findByUsernameIgnoreCase(String username);
}
