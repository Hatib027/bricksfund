package com.bricksfunds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bricksfunds.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment,Long> {

	@Query("select  p.userName,p.amount,p.dateofrequest,p.status from Payment p where p.userName=:username order by p.id desc")
	public List<?> findByUserName(String username);
	
	 @Query("select p.id, p.userName, p.amount, p.dateofrequest from Payment p where p.status = 0")
	    public List<?> fetchAllUserIfcc();
	 
	    @Query
		public List<Payment> findByStatusOrderByIdDesc(int x);
}
