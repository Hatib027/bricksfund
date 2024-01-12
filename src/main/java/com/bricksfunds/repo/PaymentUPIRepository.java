package com.bricksfunds.repo;

import com.bricksfunds.entity.Payment;
import com.bricksfunds.entity.UpiPayment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PaymentUPIRepository extends JpaRepository<UpiPayment,Long> {

	@Query("select  p.userName,p.amount,p.dateofrequest,p.status from UpiPayment p where p.userName=:username order by p.id desc")
	public List<?> findByUserName(String username);
	
	  @Query("select p.id,p.userName, p.amount, p.dateofrequest from UpiPayment p where p.status = 0")
	    public List<?> fetchAllUserUpi();
	  
	  @Query
		public List<UpiPayment> findByStatusOrderByIdDesc(int x);
}
