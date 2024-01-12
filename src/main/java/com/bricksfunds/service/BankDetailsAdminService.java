package com.bricksfunds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.BankDetailsAdmin;
import com.bricksfunds.repo.BankDetailsAdminRepository;

@Service
public class BankDetailsAdminService {

	
	@Autowired
	public BankDetailsAdminRepository bankDetailsAdminRepository;
	
	public BankDetailsAdmin addBankDetails(BankDetailsAdmin bankDetailsAdmin) {
		
		return this.bankDetailsAdminRepository.save(bankDetailsAdmin);
	}
	
	 public BankDetailsAdmin getBankDetailsAdmin(int securityCode) {
		 return this.bankDetailsAdminRepository.findBySecurityCode(securityCode);
	 }
	 
	 public BankDetailsAdmin getBanksDetailById(int id) {
		 return this.bankDetailsAdminRepository.findById(id).get();
	 }
}
