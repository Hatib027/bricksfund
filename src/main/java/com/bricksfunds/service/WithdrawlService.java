package com.bricksfunds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.InternetBankingWithdrawl;
import com.bricksfunds.entity.UPIWithdrawl;
import com.bricksfunds.entity.UpiActionAdmin;
import com.bricksfunds.repo.IbWidthrawlRepository;
import com.bricksfunds.repo.UpiActionAdminRepository;
import com.bricksfunds.repo.UpiWidthrawlRepository;

@Service
public class WithdrawlService {
	
	@Autowired
	UpiActionAdminRepository upiActionRepo;
	
	@Autowired
	private UpiWidthrawlRepository upiWidthrawlRepository;
	
	 @Autowired
	private IbWidthrawlRepository ibWidthrawlRepository;

	  
	
	public UpiActionAdmin addUpiAction(UpiActionAdmin action) {
		return this.upiActionRepo.save(action);
	}
	
	public UpiActionAdmin getUpiActionById(int id) {
		return this.upiActionRepo.findById(id).get();
	}
	
	

	public UPIWithdrawl createUpi(UPIWithdrawl upiWithdrawl){
		 return upiWidthrawlRepository.save(upiWithdrawl);
	}

	public List<?> getAllStatusZeroUpi(){
		return upiWidthrawlRepository.getAllUpiWidthrawlRequests();
	}
	
	  public InternetBankingWithdrawl createIb(InternetBankingWithdrawl internetBankingWithdrawl){
	        return ibWidthrawlRepository.save(internetBankingWithdrawl);
	    }

	    public List<?> getAllStatusZeroIb(){
	        return ibWidthrawlRepository.getAllIbWidthrawlRequests();
	 }
	    
	    public UPIWithdrawl findByIdUpi(int id) {
	    	return this.upiWidthrawlRepository.findById(id).get();
	    }
	    
	    public InternetBankingWithdrawl findByIdIb(int id) {
	    	return this.ibWidthrawlRepository.findById(id).get();
	    }
	    
	    public List<UPIWithdrawl> findByUsernameUpi(String username){
	    	return this.upiWidthrawlRepository.findByUserNameOrderByIdDesc(username);
	    }
	    
	    public List<InternetBankingWithdrawl> findByUsernameIb(String username){
	    	return this.ibWidthrawlRepository.findByUserNameOrderByIdDesc(username);
	    }

	    
	    public List<InternetBankingWithdrawl> reportIb() {
	        return ibWidthrawlRepository.findByStatusOrderByIdDesc(1);
	    }
	    public List<UPIWithdrawl> reportUpi() {
			return upiWidthrawlRepository.findByStatusOrderByIdDesc(1);
	    }

}
