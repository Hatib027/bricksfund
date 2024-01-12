package com.bricksfunds.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="investment_master")
public class InvestmentMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private Double currentInvestment;
	
	private Double totalInvestment;
	
	private int level;
	
	private String dateOfInvestment;
	
	private String dateOfExpireInvestment;
	
    private Double referralAmount;
    
    private Double totalProfit;
    
    private Double totalIntrest;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	User user;
}
