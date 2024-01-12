package com.bricksfunds.repo;

import com.bricksfunds.entity.BankMaster;
import com.bricksfunds.entity.InternetBankingWithdrawl;
import com.bricksfunds.entity.InvestmentMaster;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IbWidthrawlRepository extends JpaRepository<InternetBankingWithdrawl,Integer>{


    @Query("select p.id, p.amount,p.userName, p.holderName,p.date from InternetBankingWithdrawl p where p.status = 0")
    public List<?> getAllIbWidthrawlRequests();


    List<InternetBankingWithdrawl> findByUserName(String username);
    
    @Query
    public List<InternetBankingWithdrawl> findByStatusOrderByIdDesc(int i);

}
