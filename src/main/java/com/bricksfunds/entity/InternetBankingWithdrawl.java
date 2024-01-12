package com.bricksfunds.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="withdrawl_ib")
public class InternetBankingWithdrawl {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private Double amount;
	
	private String holderName;
	
	private Long accountNumber;
	
	private String ifscCode;
	
	private String userName;
	
	private String bankName;
	
	private String date;
	  private int status;
	

}
