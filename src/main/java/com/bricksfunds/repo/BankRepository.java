package com.bricksfunds.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bricksfunds.entity.BankDetailsAdmin;
import com.bricksfunds.entity.BankMaster;
import com.bricksfunds.entity.Role;

public interface BankRepository  extends JpaRepository<BankMaster,Integer>{
	
	
	

}
