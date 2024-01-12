package com.bricksfunds.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bricksfunds.entity.User;



public interface UserRepository extends JpaRepository<User,Long>{
 
	public User findByUsername(String username);
}
