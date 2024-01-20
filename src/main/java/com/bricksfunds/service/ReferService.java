package com.bricksfunds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bricksfunds.entity.RefPercentage;
import com.bricksfunds.entity.ReferMaster;
import com.bricksfunds.entity.User;
import com.bricksfunds.repo.ReferPercentageRepository;
import com.bricksfunds.repo.ReferReposiotry;

@Service
public class ReferService {

	@Autowired
	public ReferReposiotry referReposiotry;
	
	@Autowired
	public ReferPercentageRepository referPercentageRepository;
	
	public List<ReferMaster> getYourAmount(User user){
		
		return  this.referReposiotry.findByUserReferOrderByIdDesc(user);
	}
	
	public RefPercentage getReferPercentageById(int id) {
		return this.referPercentageRepository.findById(id).get();
	}
	
	public ReferMaster addReferData(ReferMaster ref) {
		return this.referReposiotry.save(ref);
	}
	
	public RefPercentage addReferPercentage(RefPercentage referPercentage) {
		return referPercentageRepository.save(referPercentage);
	}
	
	public String getReferDataExcel(User user) {
		return this.referReposiotry.findByReferTotal(user);
	}
	
	
}
