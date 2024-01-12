package com.bricksfunds.controller;

import java.security.Principal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bricksfunds.entity.InternetBankingWithdrawl;
import com.bricksfunds.entity.InvestmentMaster;
import com.bricksfunds.entity.UPIWithdrawl;
import com.bricksfunds.entity.UpiActionAdmin;
import com.bricksfunds.entity.User;
import com.bricksfunds.service.InvestmentMasterService;
import com.bricksfunds.service.UserService;
import com.bricksfunds.service.WithdrawlService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/withdrawl")
public class WithdrawlController {
	
	@Autowired
	InvestmentMasterService investmentMasterService;
	
	@Autowired
	UserService userService;
	
	
	    @Autowired
	    private WithdrawlService  withdrawlService;
	   


	    @PostMapping("/ib-widthrawl")
	    public InternetBankingWithdrawl ibWidthrawl(@RequestBody String data){
	        Gson g = new Gson();
	        InternetBankingWithdrawl s = g.fromJson(data, InternetBankingWithdrawl.class);
	        
	        SimpleDateFormat sj = new SimpleDateFormat("dd-MM-yyyy");
	        
	        String date = sj.format(new Date());
	        s.setDate(date);
	        return withdrawlService.createIb(s);
	    }

	    @PostMapping("/upi-widthrawl")
	    public UPIWithdrawl upiWidthrawl(@RequestBody String data){
	        Gson g = new Gson();
	        UPIWithdrawl s = g.fromJson(data, UPIWithdrawl.class);
SimpleDateFormat sj = new SimpleDateFormat("dd-MM-yyyy");
	        
	        String date = sj.format(new Date());
	        s.setDate(date);
	      return  withdrawlService.createUpi(s);
	    }
	    @PostMapping("/upi-widthrawl-all")
	    public List<?> getAllUpiWidthrawlRequestUpi(){
	      return  withdrawlService.getAllStatusZeroUpi();
	    }

	    @PostMapping("/ib-widthrawl-all")
	    public List<?> getAllUpiWidthrawlRequestIb(){
	        return  withdrawlService.getAllStatusZeroIb();
	    }
	
	@GetMapping("/getUserInvestment")
	public InvestmentMaster getUserInvestment(HttpServletRequest request) {
		Principal p = request.getUserPrincipal();
		
		User user = this.userService.getUser(p.getName());
		
		InvestmentMaster i = this.investmentMasterService.findByUserId(user);
		return i;
	}
	
	@GetMapping("/getUpiAction")
	public UpiActionAdmin getUpiAction() {
		UpiActionAdmin a = new UpiActionAdmin();
		
		try {
			a=this.withdrawlService.getUpiActionById(1);
		}catch(Exception e) {
			
		}
		return a;
	}
	
	
	@GetMapping("/getWithdrawlRequestUpi")
	public List<UPIWithdrawl> getWithdrawlRequestUpi(HttpServletRequest request) {
		Principal p = request.getUserPrincipal();
		String username = p.getName();
		
		return this.withdrawlService.findByUsernameUpi(username);
	}
	

	@GetMapping("/getWithdrawlRequestIb")
	public List<InternetBankingWithdrawl> getWithdrawlRequestIb(HttpServletRequest request) {
		Principal p = request.getUserPrincipal();
		String username = p.getName();
		
		return this.withdrawlService.findByUsernameIb(username);
	}

}
