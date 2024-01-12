package com.bricksfunds.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bricksfunds.entity.BankDetailsAdmin;

public interface BankDetailsAdminRepository extends JpaRepository<BankDetailsAdmin,Integer>{

	public BankDetailsAdmin findBySecurityCode(int code);
}
