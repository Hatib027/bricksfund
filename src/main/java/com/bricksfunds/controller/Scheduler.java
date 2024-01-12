package com.bricksfunds.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.bricksfunds.entity.InvestmentMaster;
import com.bricksfunds.service.InvestmentMasterService;

@Component
public class Scheduler {
	
	 private static final DecimalFormat dfs = new DecimalFormat("0.000");
	
	@Autowired
	InvestmentMasterService investmentMasterService;
	
	 @Scheduled(cron = "0 10 00 ? * *") 
	 public void scheduleForLevel1()
	    {
		
		 Date d = new Date();
		 SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
			String dateOfToday = ds.format(d);
	 
			
			System.out.println(new Date());
	     List<InvestmentMaster> investment =  (List<InvestmentMaster>) this.investmentMasterService.getLevel1Scheduler(dateOfToday);
	     
	     for(InvestmentMaster i : investment) {
	    	 InvestmentMaster ij = new InvestmentMaster();
	    	 Double amount = (i.getCurrentInvestment() * 1)/100;
	    	 
	    	 
	    	 
	    	 Double mainAmount = i.getCurrentInvestment();
	    	 Double  amtIntrestPrevious = i.getTotalIntrest();
	    	 
	    	 if(i.getTotalIntrest() == null || i.getTotalIntrest() ==0) {
	    		 ij.setTotalIntrest(amount);
	    	 }else {
	    		 Double amt = amtIntrestPrevious+amount;
	    		 ij.setTotalIntrest(Double.parseDouble(dfs.format(amt)));
	    	 }
	    	 
	    	 if(i.getTotalProfit() == null || i.getTotalProfit() == 0) {
	    		 ij.setTotalProfit(Double.parseDouble(dfs.format(amount)));
	    	 }else {
	    		 Double profitCount = Double.parseDouble(dfs.format(i.getTotalProfit()+amount));
	    		 ij.setTotalProfit(profitCount);
	    	 }
	    	 
	    	 Double amt =  Double.parseDouble(dfs.format(mainAmount)) ;
	    	 ij.setCurrentInvestment(amt);
	    	 ij.setDateOfExpireInvestment(i.getDateOfExpireInvestment());
	    	 ij.setDateOfInvestment(i.getDateOfInvestment());
	    	 ij.setId(i.getId());
	    	 ij.setLevel(i.getLevel());
	    	 ij.setTotalInvestment(i.getTotalInvestment());
	    	 ij.setUser(i.getUser());
	    	 ij.setReferralAmount(i.getReferralAmount());
	    	 
	    	 InvestmentMaster im = this.investmentMasterService.addInvestment(ij);
	     }
	    
	    }
	 
	 
	@Scheduled(cron = "0 05 00 ? * *")
	 public void scheduleForLevel2()
	    {
		 Date d = new Date();
		 SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
			String dateOfToday = ds.format(d);
	 
			System.out.println(new Date());
			
	     List<InvestmentMaster> investment =  (List<InvestmentMaster>) this.investmentMasterService.getLevel2Scheduler(dateOfToday);
	     
	     for(InvestmentMaster i : investment) {
	    	 InvestmentMaster ij = new InvestmentMaster();
	    	 Double amount = (i.getCurrentInvestment() * 1.667)/100;
	    	 
	    	 Double mainAmount = i.getCurrentInvestment();
	    	 
	    
	    	 Double  amtIntrestPrevious = i.getTotalIntrest();
	    	 
	    	 if(i.getTotalIntrest() == null || i.getTotalIntrest() ==0) {
	    		 ij.setTotalIntrest(amount);
	    	 }else {
	    		 Double amt = amtIntrestPrevious+amount;
	    		 ij.setTotalIntrest(Double.parseDouble(dfs.format(amt)));
	    	 }
	    	 
	    	 if(i.getTotalProfit() == null || i.getTotalProfit() == 0) {
	    		 ij.setTotalProfit(Double.parseDouble(dfs.format(amount)));
	    	 }else {
	    		 Double profitCount = Double.parseDouble(dfs.format(i.getTotalProfit()+amount));
	    		 ij.setTotalProfit(profitCount);
	    	 }
	    	 Double amt =  Double.parseDouble(dfs.format(mainAmount)) ;
	    	 ij.setCurrentInvestment(amt);
	    	 ij.setDateOfExpireInvestment(i.getDateOfExpireInvestment());
	    	 ij.setDateOfInvestment(i.getDateOfInvestment());
	    	 ij.setId(i.getId());
	    	 ij.setLevel(i.getLevel());
	    	 ij.setTotalInvestment(i.getTotalInvestment());
	    	 ij.setUser(i.getUser()); 
	    	 ij.setReferralAmount(i.getReferralAmount());
	    	 InvestmentMaster im = this.investmentMasterService.addInvestment(ij);
	     }
	    
	    }
	 
	@Scheduled(cron = "0 01 00 ? * *")
	 public void scheduleForLevel3()
	    {
		 Date d = new Date();
		 SimpleDateFormat ds =new SimpleDateFormat("dd-MM-yyyy");
			String dateOfToday = ds.format(d);
	 
		
			
	     List<InvestmentMaster> investment =  (List<InvestmentMaster>) this.investmentMasterService.getLevel3Scheduler(dateOfToday);
	     
	     for(InvestmentMaster i : investment) {
	    	 InvestmentMaster ij = new InvestmentMaster();
	    	 Double amount = (i.getCurrentInvestment() * 5)/100;
	    	 
	    	 Double mainAmount = i.getCurrentInvestment() ;
	    	
	    	 Double  amtIntrestPrevious = i.getTotalIntrest();
	    	 
	    	 if(i.getTotalIntrest() == null || i.getTotalIntrest() ==0) {
	    		 ij.setTotalIntrest(amount);
	    	 }else {
	    		 Double amt = amtIntrestPrevious+amount;
	    		 ij.setTotalIntrest(Double.parseDouble(dfs.format(amt)));
	    	 }
	    	 
	    	 if(i.getTotalProfit() == null || i.getTotalProfit() == 0) {
	    		 ij.setTotalProfit(Double.parseDouble(dfs.format(amount)));
	    	 }else {
	    		 Double profitCount = Double.parseDouble(dfs.format(i.getTotalProfit()+amount));
	    		 ij.setTotalProfit(profitCount);
	    	 }
	    	 Double amt =  Double.parseDouble(dfs.format(mainAmount)) ;
	    	 ij.setCurrentInvestment(amt);
	    	 ij.setDateOfExpireInvestment(i.getDateOfExpireInvestment());
	    	 ij.setDateOfInvestment(i.getDateOfInvestment());
	    	 ij.setId(i.getId());
	    	 ij.setLevel(i.getLevel());
	    	 ij.setTotalInvestment(i.getTotalInvestment());
	    	 ij.setUser(i.getUser());
	    	 ij.setReferralAmount(i.getReferralAmount());
	    	 
	    	 InvestmentMaster im = this.investmentMasterService.addInvestment(ij);
	     }
	    
	    }
}
