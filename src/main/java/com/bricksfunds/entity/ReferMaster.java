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
@Table(name="ref_master")
public class ReferMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user_id")
	User userRefer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="friend_id")
	User friendRefer;
	
	private double amount;
	
	private String Date;
}
