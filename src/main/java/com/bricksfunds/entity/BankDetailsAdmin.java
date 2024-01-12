package com.bricksfunds.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="admin_bankdetails")
public class BankDetailsAdmin {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String accountNumber;
	String ifscCode;
	String upiId;
	int securityCode;
	String accountHoldername;
	
	public BankDetailsAdmin() {
		
	}
	

}
