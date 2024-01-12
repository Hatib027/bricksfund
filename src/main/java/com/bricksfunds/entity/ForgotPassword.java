package com.bricksfunds.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="forgot_pass")
public class ForgotPassword {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	private String email;
	
	private String token;
	
	private String expire;
	
	private String time;
}
