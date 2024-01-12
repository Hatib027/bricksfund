package com.bricksfunds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.BankMaster;
import com.bricksfunds.repo.BankRepository;

@Service
public class BankService {

	@Autowired
	private BankRepository bankRepo;
	
	
	public List<BankMaster> getAllBank(){
		return this.bankRepo.findAll();
	}
	
}
