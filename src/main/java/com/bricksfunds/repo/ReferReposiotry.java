package com.bricksfunds.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bricksfunds.entity.ReferMaster;
import com.bricksfunds.entity.User;

public interface ReferReposiotry extends JpaRepository<ReferMaster, Integer>{

	
	public List<ReferMaster> findByUserRefer(User userId);
	
	@Query("select sum(r.amount) from ReferMaster r where r.userRefer=:refer")
	public String findByReferTotal(User refer);
}
