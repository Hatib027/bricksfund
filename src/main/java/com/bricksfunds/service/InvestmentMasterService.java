package com.bricksfunds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.InvestmentMaster;
import com.bricksfunds.entity.User;
import com.bricksfunds.repo.InvestmentMasterRepository;

@Service
public class InvestmentMasterService {
	
	@Autowired
	public InvestmentMasterRepository investmentMasterRepository;
	
	public InvestmentMaster findByUserId(User userId) {
		return this.investmentMasterRepository.findByUser(userId);
	}
	
	public InvestmentMaster addInvestment(InvestmentMaster i) {
		return this.investmentMasterRepository.save(i);
	}
	
	public List<?> getLevel1Scheduler(String date){
		return this.investmentMasterRepository.findbySchedulingLevel1(date);
	}
	
	public List<?> getLevel2Scheduler(String date){
		return this.investmentMasterRepository.findbySchedulingLevel2(date);
	}
	
	public List<?> getLevel3Scheduler(String date){
		return this.investmentMasterRepository.findbySchedulingLevel3(date);
	}
	
	public List<String> getTotal(){
		return this.investmentMasterRepository.findTotalForAdmin();
	}

	
	public List<InvestmentMaster> findForReportLevel(){
		return this.investmentMasterRepository.findAllByOrderByLevelDesc();
	}
	
	public String findTotalByReferExcel(String referCode) {
		return this.investmentMasterRepository.findByInvestmentRaised(referCode);
	}
}
