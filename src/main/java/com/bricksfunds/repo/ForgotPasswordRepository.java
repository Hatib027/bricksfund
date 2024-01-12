package com.bricksfunds.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bricksfunds.entity.ForgotPassword;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer>{

	
	public ForgotPassword findByEmail(String email);
	
	public ForgotPassword findByToken(String token);
}
