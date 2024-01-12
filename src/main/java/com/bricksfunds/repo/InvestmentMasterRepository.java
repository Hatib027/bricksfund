package com.bricksfunds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bricksfunds.entity.InternetBankingWithdrawl;
import com.bricksfunds.entity.InvestmentMaster;
import com.bricksfunds.entity.User;

public interface InvestmentMasterRepository extends JpaRepository<InvestmentMaster, Integer>{

	public InvestmentMaster findByUser(User userId);
	

	@Query("select i from InvestmentMaster i where i.level=1 and i.dateOfExpireInvestment != '' and str_to_date(dateOfExpireInvestment, '%d-%m-%Y') >= str_to_date(:date, '%d-%m-%Y')")
	public List<?> findbySchedulingLevel1(String date);
	
	@Query("select i from InvestmentMaster i where i.level=2 and i.dateOfExpireInvestment != '' and str_to_date(dateOfExpireInvestment, '%d-%m-%Y') >= str_to_date(:date, '%d-%m-%Y')")
	public List<?> findbySchedulingLevel2(String date);
	
	@Query("select i from InvestmentMaster i where i.level=3 and i.dateOfExpireInvestment != '' and str_to_date(dateOfExpireInvestment, '%d-%m-%Y') >= str_to_date(:date, '%d-%m-%Y')")
	public List<?> findbySchedulingLevel3(String date);
	
	
	@Query("select sum(i.currentInvestment),sum(i.totalInvestment) from InvestmentMaster i")
	public List<String> findTotalForAdmin();
	
	 List<InvestmentMaster> findAllByOrderByLevelDesc();
	 
	 
	   @Query("select sum(i.totalInvestment) from InvestmentMaster i where i.user.referralcode=:referCode")
		public String findByInvestmentRaised(String referCode);
	

}
