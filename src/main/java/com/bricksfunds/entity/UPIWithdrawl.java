package com.bricksfunds.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="withdrawl_upi")
public class UPIWithdrawl implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private Double amount;
	
	private String holderName;
	
	
	private String upiId;
	
	private String userName;
	
	private String Date;

	private int status;
}
