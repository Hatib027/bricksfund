package com.bricksfunds.repo;

import com.bricksfunds.entity.BankMaster;
import com.bricksfunds.entity.UPIWithdrawl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UpiWidthrawlRepository extends JpaRepository<UPIWithdrawl,Integer>{

    @Query("select p.id, p.userName, p.amount,p.userName, p.holderName,p.Date from UPIWithdrawl p where p.status = 0")
    public List<?> getAllUpiWidthrawlRequests();
    
    public List<UPIWithdrawl> findByUserNameOrderByIdDesc(String username);
    
    public  List<UPIWithdrawl> findByStatusOrderByIdDesc(int i);
}




